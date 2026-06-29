package empire.digiprem.kmptemplate.feature.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email    : String,
    val password : String,
)

@Serializable
data class LoginResponse(
    val accessToken  : String = "",
    val refreshToken : String = "",
    val expiresAt    : Long   = 0L,
    val userId       : String = "",
    val isVerified   : Boolean = false,
)

@Serializable
data class ForgotPasswordRequest(val email: String)

@Serializable
data class ResetPasswordRequest(
    val token       : String,
    val newPassword : String,
)

@Serializable
data class VerifyIdentityRequest(val code: String)

@Serializable
data class VerifyIdentityResponse(val isVerified: Boolean = false)

@Serializable
data class CreateProfileRequest(
    val firstName : String,
    val lastName  : String,
    val username  : String,
)

@Serializable
data class CreateProfileResponse(
    val id        : String = "",
    val email     : String = "",
    val firstName : String = "",
    val lastName  : String = "",
    val username  : String = "",
    val avatarUrl : String? = null,
)
