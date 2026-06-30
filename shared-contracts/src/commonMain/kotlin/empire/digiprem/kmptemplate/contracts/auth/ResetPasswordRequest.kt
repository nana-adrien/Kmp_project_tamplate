package empire.digiprem.kmptemplate.contracts.auth

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRequest(
    val token: String,
    val newPassword: String,
)
