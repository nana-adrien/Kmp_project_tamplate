package empire.digiprem.kmptemplate.feature.auth.mapper

import empire.digiprem.kmptemplate.feature.auth.dto.CreateProfileResponse
import empire.digiprem.kmptemplate.feature.auth.dto.LoginResponse
import empire.digiprem.kmptemplate.feature.auth.model.AuthSession
import empire.digiprem.kmptemplate.feature.auth.model.UserProfile

fun LoginResponse.toDomain() = AuthSession(
    accessToken  = accessToken,
    refreshToken = refreshToken,
    expiresAt    = expiresAt,
    userId       = userId,
    isVerified   = isVerified,
)

fun CreateProfileResponse.toDomain() = UserProfile(
    id        = id,
    email     = email,
    firstName = firstName,
    lastName  = lastName,
    username  = username,
    avatarUrl = avatarUrl,
)
