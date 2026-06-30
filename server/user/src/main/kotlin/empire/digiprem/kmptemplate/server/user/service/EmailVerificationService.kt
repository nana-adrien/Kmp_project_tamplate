package empire.digiprem.kmptemplate.server.user.service

import empire.digiprem.kmptemplate.server.common.exception.InvalidTokenException
import empire.digiprem.kmptemplate.server.common.exception.UserNotFoundException
import empire.digiprem.kmptemplate.server.user.infra.entity.EmailVerificationTokenEntity
import empire.digiprem.kmptemplate.server.user.infra.entity.UserEntity
import empire.digiprem.kmptemplate.server.user.infra.repository.EmailVerificationTokenRepository
import empire.digiprem.kmptemplate.server.user.infra.repository.UserRepository
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class EmailVerificationService(
    private val tokenRepository: EmailVerificationTokenRepository,
    private val userRepository: UserRepository,
    private val mailSender: JavaMailSender?,
    @Value("\${app.frontend-url:http://localhost:3000}") private val frontendUrl: String,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun createToken(email: String): String {
        tokenRepository.deleteByEmail(email)
        val token = UUID.randomUUID().toString()
        tokenRepository.save(
            EmailVerificationTokenEntity(
                email = email,
                token = token,
                expiresAt = Instant.now().plusSeconds(24 * 60 * 60),
            )
        )
        return token
    }

    @Transactional
    fun verifyEmail(token: String) {
        val record = tokenRepository.findByToken(token)
            ?: throw InvalidTokenException("Verification token is invalid or has already been used")

        if (record.expiresAt.isBefore(Instant.now())) {
            tokenRepository.delete(record)
            throw InvalidTokenException("Verification token has expired")
        }

        val user = userRepository.findByEmail(record.email)
            ?: throw UserNotFoundException()

        val verifiedUser = UserEntity(
            id = user.id,
            email = user.email,
            username = user.username,
            hashedPassword = user.hashedPassword,
            hasVerifiedEmail = true,
            createdAt = user.createdAt,
        )
        userRepository.save(verifiedUser)
        tokenRepository.delete(record)
    }

    fun sendVerificationEmail(email: String, token: String) {
        val verificationLink = "$frontendUrl/verify-email?token=$token"
        if (mailSender != null) {
            try {
                val message = mailSender.createMimeMessage()
                val helper = MimeMessageHelper(message, false, "UTF-8")
                helper.setTo(email)
                helper.setSubject("Verify your email address")
                helper.setText(
                    "Please click the link below to verify your email address:\n\n$verificationLink\n\nThis link expires in 24 hours.",
                    false,
                )
                mailSender.send(message)
                logger.info("Verification email sent to $email")
            } catch (e: Exception) {
                logger.error("Failed to send verification email to $email: ${e.message}")
            }
        } else {
            logger.info("Mail not configured — verification link for $email: $verificationLink")
        }
    }
}
