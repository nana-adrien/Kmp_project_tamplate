package empire.digiprem.kmptemplate.contracts.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val userId: String,
    val email: String,
    val username: String,
)
