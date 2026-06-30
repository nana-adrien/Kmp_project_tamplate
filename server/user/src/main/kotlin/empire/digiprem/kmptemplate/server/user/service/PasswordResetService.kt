package empire.digiprem.kmptemplate.server.user.service

import empire.digiprem.kmptemplate.contracts.auth.ForgotPasswordRequest
import empire.digiprem.kmptemplate.contracts.auth.ResetPasswordRequest
import empire.digiprem.kmptemplate.contracts.validation.validateForgotPasswordRequest
import empire.digiprem.kmptemplate.contracts.validation.validateResetPasswordRequest
import empire.digiprem.kmptemplate.server.common.exception.InvalidTokenException
import empire.digiprem.kmptemplate.server.common.exception.UserNotFoundException
import empire.digiprem.kmptemplate.server.common.security.PasswordEncoder
import empire.digiprem.kmptemplate.server.common.util.orThrow
import empire.digiprem.kmptemplate.server.user.infra.entity.PasswordResetTokenEntity
import empire.digiprem.kmptemplate.server.user.infra.entity.UserEntity
import empire.digiprem.kmptemplate.server.user.infra.repository.PasswordResetTokenRepository
import empire.digiprem.kmptemplate.server.user.infra.repository.UserRepository
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.time.Instant
import java.util.Base64
import java.util.UUID

@Service
class PasswordResetService(
    private val userRepository: UserRepository,
    private val passwordResetTokenRepository: PasswordResetTokenRepository,
    private val passwordEncoder: PasswordEncoder,
    private val mailSender: JavaMailSender?,
    @Value("\${app.frontend-url:http://localhost:3000}") private val frontendUrl: String,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun requestReset(email: String) {
        validateForgotPasswordRequest(ForgotPasswordRequest(email)).orThrow()
        val user = userRepository.findByEmail(email.trim()) ?: return // silent — don't reveal if email exists
        val rawToken = UUID.randomUUID().toString()
        val hashed = hashToken(rawToken)

        user.id?.let { userId ->
            passwordResetTokenRepository.deleteByUserId(userId)
            passwordResetTokenRepository.save(
                PasswordResetTokenEntity(
                    userId = userId,
                    hashedToken = hashed,
                    expiresAt = Instant.now().plusSeconds(60 * 60), // 1 hour
                )
            )
        }

        sendPasswordResetEmail(email, rawToken)
    }

    @Transactional
    fun resetPassword(token: String, newPassword: String) {
        validateResetPasswordRequest(ResetPasswordRequest(token, newPassword)).orThrow()
        val hashed = hashToken(token)
        val record = passwordResetTokenRepository.findByHashedToken(hashed)
            ?: throw InvalidTokenException("Password reset token is invalid or has already been used")

        if (record.expiresAt.isBefore(Instant.now())) {
            passwordResetTokenRepository.delete(record)
            throw InvalidTokenException("Password reset token has expired")
        }

        val user = userRepository.findById(record.userId).orElseThrow { UserNotFoundException() }
        val updatedUser = UserEntity(
            id = user.id,
            email = user.email,
            username = user.username,
            hashedPassword = passwordEncoder.encode(newPassword),
            hasVerifiedEmail = user.hasVerifiedEmail,
            createdAt = user.createdAt,
        )
        userRepository.save(updatedUser)
        passwordResetTokenRepository.delete(record)
    }

    private fun sendPasswordResetEmail(email: String, token: String) {
        val resetLink = "$frontendUrl/reset-password?token=$token"
        if (mailSender != null) {
            try {
                val message = mailSender.createMimeMessage()
                val helper = MimeMessageHelper(message, false, "UTF-8")
                helper.setTo(email)
                helper.setSubject("Reset your password")
                helper.setText(
                    "Click the link below to reset your password:\n\n$resetLink\n\nThis link expires in 1 hour. If you did not request a password reset, ignore this email.",
                    false,
                )
                mailSender.send(message)
                logger.info("Password reset email sent to $email")
            } catch (e: Exception) {
                logger.error("Failed to send password reset email to $email: ${e.message}")
            }
        } else {
            logger.info("Mail not configured — password reset link for $email: $resetLink")
        }
    }

    private fun hashToken(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(token.encodeToByteArray())
        return Base64.getEncoder().encodeToString(hashBytes)
    }
}
