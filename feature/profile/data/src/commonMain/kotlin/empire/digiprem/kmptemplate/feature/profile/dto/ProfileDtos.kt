package empire.digiprem.kmptemplate.feature.profile.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    val id        : String,
    val email     : String,
    val firstName : String,
    val lastName  : String,
    val username  : String  = "",
    val avatarUrl : String? = null,
    val bio       : String? = null,
)

@Serializable
data class UpdateProfileRequest(
    val firstName : String,
    val lastName  : String,
    val username  : String,
    val bio       : String? = null,
)
