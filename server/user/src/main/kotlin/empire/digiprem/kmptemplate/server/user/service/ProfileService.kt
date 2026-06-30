package empire.digiprem.kmptemplate.server.user.service

import empire.digiprem.kmptemplate.server.common.exception.UserNotFoundException
import empire.digiprem.kmptemplate.server.user.infra.entity.ProfileEntity
import empire.digiprem.kmptemplate.server.user.infra.repository.ProfileRepository
import empire.digiprem.kmptemplate.contracts.profile.ProfileDto
import empire.digiprem.kmptemplate.contracts.profile.UpdateProfileRequest
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ProfileService(
    private val profileRepository: ProfileRepository,
) {

    fun getProfile(userId: String): ProfileDto {
        val uuid = UUID.fromString(userId)
        val profile = profileRepository.findById(uuid).orElseThrow { UserNotFoundException() }
        return profile.toDto()
    }

    @Transactional
    fun updateProfile(userId: String, request: UpdateProfileRequest): ProfileDto {
        val uuid = UUID.fromString(userId)
        val existing = profileRepository.findById(uuid).orElseThrow { UserNotFoundException() }
        val updated = ProfileEntity(
            userId = existing.userId,
            displayName = request.displayName ?: existing.displayName,
            bio = request.bio ?: existing.bio,
            avatarUrl = existing.avatarUrl,
        )
        return profileRepository.save(updated).toDto()
    }

    private fun ProfileEntity.toDto() = ProfileDto(
        userId = userId.toString(),
        displayName = displayName,
        bio = bio,
        avatarUrl = avatarUrl,
    )
}
