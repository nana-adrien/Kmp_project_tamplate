package empire.digiprem.kmptemplate.contracts.profile

import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    val userId: String,
    val displayName: String,
    val bio: String?,
    val avatarUrl: String?,
)
