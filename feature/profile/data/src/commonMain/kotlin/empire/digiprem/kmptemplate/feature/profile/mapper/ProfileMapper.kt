package empire.digiprem.kmptemplate.feature.profile.mapper

import empire.digiprem.kmptemplate.feature.profile.dto.ProfileDto
import empire.digiprem.kmptemplate.feature.profile.dto.UpdateProfileRequest
import empire.digiprem.kmptemplate.feature.profile.model.Profile

fun ProfileDto.toDomain(): Profile = Profile(
    id        = id,
    email     = email,
    firstName = firstName,
    lastName  = lastName,
    username  = username,
    avatarUrl = avatarUrl,
    bio       = bio,
)

fun Profile.toUpdateRequest(): UpdateProfileRequest = UpdateProfileRequest(
    firstName = firstName,
    lastName  = lastName,
    username  = username,
    bio       = bio,
)
