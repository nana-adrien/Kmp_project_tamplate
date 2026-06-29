package empire.digiprem.kmptemplate.feature.profile.usecase

import empire.digiprem.kmptemplate.core.domain.util.Result
import empire.digiprem.kmptemplate.core.domain.util.mapFailure
import empire.digiprem.kmptemplate.feature.profile.error.ProfileError
import empire.digiprem.kmptemplate.feature.profile.model.Profile
import empire.digiprem.kmptemplate.feature.profile.repository.IProfileRepository

class GetProfileUseCase(private val repository: IProfileRepository) {
    suspend operator fun invoke(): Result<Profile?, ProfileError> =
        repository.getProfile().mapFailure { ProfileError.Unknown }
}
