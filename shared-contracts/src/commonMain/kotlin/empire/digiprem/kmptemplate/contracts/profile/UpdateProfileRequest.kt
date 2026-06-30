package empire.digiprem.kmptemplate.contracts.profile

import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileRequest(
    val displayName: String?,
    val bio: String?,
)
