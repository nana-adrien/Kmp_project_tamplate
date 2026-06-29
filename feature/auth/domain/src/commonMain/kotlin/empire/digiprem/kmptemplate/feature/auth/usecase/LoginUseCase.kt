package empire.digiprem.kmptemplate.feature.auth.usecase

import empire.digiprem.kmptemplate.core.domain.network.DataError
import empire.digiprem.kmptemplate.core.domain.util.Result
import empire.digiprem.kmptemplate.core.domain.util.mapFailure
import empire.digiprem.kmptemplate.feature.auth.error.AuthError
import empire.digiprem.kmptemplate.feature.auth.model.AuthSession
import empire.digiprem.kmptemplate.feature.auth.repository.IAuthRepository

class LoginUseCase(private val repository: IAuthRepository) {
    suspend operator fun invoke(
        email   : String,
        password: String,
    ): Result<AuthSession?, AuthError> =
        repository.login(email, password)
            .mapFailure { dataError ->
                when (dataError) {
                    DataError.Remote.Unauthorized -> AuthError.InvalidCredentials
                    DataError.Remote.Forbidden    -> AuthError.AccountLocked
                    DataError.Remote.NotFound     -> AuthError.InvalidCredentials
                    else                          -> AuthError.Unknown
                }
            }
}
