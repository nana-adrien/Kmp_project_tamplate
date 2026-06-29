package empire.digiprem.kmptemplate.feature.auth.usecase

import empire.digiprem.kmptemplate.core.domain.util.Result
import empire.digiprem.kmptemplate.core.domain.util.mapFailure
import empire.digiprem.kmptemplate.feature.auth.error.AuthError
import empire.digiprem.kmptemplate.feature.auth.repository.IAuthRepository

class ForgotPasswordUseCase(private val repository: IAuthRepository) {
    suspend operator fun invoke(email: String): Result<Unit?, AuthError> =
        repository.forgotPassword(email)
            .mapFailure { AuthError.Unknown }
}
