package empire.digiprem.kmptemplate.feature.profile.usecase

import empire.digiprem.kmptemplate.core.domain.util.Result
import empire.digiprem.kmptemplate.core.domain.util.mapFailure
import empire.digiprem.kmptemplate.feature.profile.error.ProfileError
import empire.digiprem.kmptemplate.feature.profile.model.Profile
import empire.digiprem.kmptemplate.feature.profile.repository.IProfileRepository

class UpdateProfileUseCase(private val repository: IProfileRepository) {
    suspend operator fun invoke(profile: Profile): Result<Profile?, ProfileError> =
        repository.updateProfile(profile).mapFailure { ProfileError.Unknown }
}
