package empire.digiprem.kmptemplate.feature.auth.usecase

import empire.digiprem.kmptemplate.core.domain.util.Result
import empire.digiprem.kmptemplate.core.domain.util.mapFailure
import empire.digiprem.kmptemplate.feature.auth.error.AuthError
import empire.digiprem.kmptemplate.feature.auth.model.UserProfile
import empire.digiprem.kmptemplate.feature.auth.repository.IAuthRepository

class CreateProfileUseCase(private val repository: IAuthRepository) {
    suspend operator fun invoke(
        firstName: String,
        lastName : String,
        username : String,
    ): Result<UserProfile?, AuthError> =
        repository.createProfile(firstName, lastName, username)
            .mapFailure { AuthError.Unknown }
}
