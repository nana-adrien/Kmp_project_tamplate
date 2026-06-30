package empire.digiprem.kmptemplate.contracts.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String,
)
