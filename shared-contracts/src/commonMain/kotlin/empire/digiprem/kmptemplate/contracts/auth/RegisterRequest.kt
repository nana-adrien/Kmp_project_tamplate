package empire.digiprem.kmptemplate.contracts.auth

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val email: String,
    val username: String,
    val password: String,
)
