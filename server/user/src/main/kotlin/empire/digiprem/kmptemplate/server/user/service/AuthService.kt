package empire.digiprem.kmptemplate.server.user.service

import empire.digiprem.kmptemplate.server.common.exception.EmailNotVerifiedException
import empire.digiprem.kmptemplate.server.common.exception.InvalidCredentialsException
import empire.digiprem.kmptemplate.server.common.exception.InvalidTokenException
import empire.digiprem.kmptemplate.server.common.exception.UserAlreadyExistsException
import empire.digiprem.kmptemplate.server.common.exception.UserNotFoundException
import empire.digiprem.kmptemplate.server.common.security.JwtService
import empire.digiprem.kmptemplate.server.common.security.PasswordEncoder
import empire.digiprem.kmptemplate.server.user.infra.entity.ProfileEntity
import empire.digiprem.kmptemplate.server.user.infra.entity.RefreshTokenEntity
import empire.digiprem.kmptemplate.server.user.infra.entity.UserEntity
import empire.digiprem.kmptemplate.server.user.infra.repository.ProfileRepository
import empire.digiprem.kmptemplate.server.user.infra.repository.RefreshTokenRepository
import empire.digiprem.kmptemplate.server.user.infra.repository.UserRepository
import empire.digiprem.kmptemplate.contracts.auth.AuthResponse
import empire.digiprem.kmptemplate.contracts.auth.LoginRequest
import empire.digiprem.kmptemplate.contracts.auth.RegisterRequest
import empire.digiprem.kmptemplate.contracts.validation.validateLoginRequest
import empire.digiprem.kmptemplate.contracts.validation.validateRegisterRequest
import empire.digiprem.kmptemplate.server.common.util.orThrow
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.time.Instant
import java.util.Base64
import java.util.UUID

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val profileRepository: ProfileRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val emailVerificationService: EmailVerificationService,
) {

    @Transactional
    fun register(email: String, username: String, password: String): Unit {
        validateRegisterRequest(RegisterRequest(email, username, password)).orThrow()
        val trimmedEmail = email.trim()
        val trimmedUsername = username.trim()

        if (userRepository.findByEmailOrUsername(trimmedEmail, trimmedUsername) != null) {
            throw UserAlreadyExistsException()
        }

        val savedUser = userRepository.save(
            UserEntity(
                email = trimmedEmail,
                username = trimmedUsername,
                hashedPassword = passwordEncoder.encode(password),
            )
        )

        savedUser.id?.let { userId ->
            profileRepository.save(
                ProfileEntity(
                    userId = userId,
                    displayName = trimmedUsername,
                )
            )
        }

        val verificationToken = emailVerificationService.createToken(trimmedEmail)
        emailVerificationService.sendVerificationEmail(trimmedEmail, verificationToken)
    }

    fun login(email: String, password: String): AuthResponse {
        validateLoginRequest(LoginRequest(email, password)).orThrow()
        val user = userRepository.findByEmail(email.trim())
            ?: throw InvalidCredentialsException()

        if (!passwordEncoder.matches(password, user.hashedPassword)) {
            throw InvalidCredentialsException()
        }

        if (!user.hasVerifiedEmail) {
            throw EmailNotVerifiedException()
        }

        val userId = user.id ?: throw UserNotFoundException()

        val accessToken = jwtService.generateAccessToken(userId.toString())
        val refreshToken = jwtService.generateRefreshToken(userId.toString())
        storeRefreshToken(userId, refreshToken)

        return AuthResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            userId = userId.toString(),
            email = user.email,
            username = user.username,
        )
    }

    @Transactional
    fun refresh(refreshToken: String): AuthResponse {
        if (!jwtService.validateRefreshToken(refreshToken)) {
            throw InvalidTokenException("Invalid refresh token")
        }

        val userIdStr = jwtService.getUserIdFromToken(refreshToken)
        val userId = UUID.fromString(userIdStr)
        val user = userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException()

        val hashed = hashToken(refreshToken)
        refreshTokenRepository.findByUserIdAndHashedToken(userId, hashed)
            ?: throw InvalidTokenException("Refresh token not found or already used")

        refreshTokenRepository.deleteByUserIdAndHashedToken(userId, hashed)

        val newAccessToken = jwtService.generateAccessToken(userId.toString())
        val newRefreshToken = jwtService.generateRefreshToken(userId.toString())
        storeRefreshToken(userId, newRefreshToken)

        return AuthResponse(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken,
            userId = userId.toString(),
            email = user.email,
            username = user.username,
        )
    }

    @Transactional
    fun logout(refreshToken: String) {
        runCatching {
            val userIdStr = jwtService.getUserIdFromToken(refreshToken)
            val userId = UUID.fromString(userIdStr)
            val hashed = hashToken(refreshToken)
            refreshTokenRepository.deleteByUserIdAndHashedToken(userId, hashed)
        }
    }

    private fun storeRefreshToken(userId: UUID, token: String) {
        val hashed = hashToken(token)
        val expiresAt = Instant.now().plusMillis(jwtService.refreshTokenExpiryMsValue)
        refreshTokenRepository.save(
            RefreshTokenEntity(
                userId = userId,
                hashedToken = hashed,
                expiresAt = expiresAt,
            )
        )
    }

    private fun hashToken(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(token.encodeToByteArray())
        return Base64.getEncoder().encodeToString(hashBytes)
    }
}
