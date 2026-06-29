package empire.digiprem.kmptemplate.feature.auth.usecase

import empire.digiprem.kmptemplate.core.domain.network.DataError
import empire.digiprem.kmptemplate.core.domain.util.Result
import empire.digiprem.kmptemplate.core.domain.util.mapFailure
import empire.digiprem.kmptemplate.feature.auth.error.AuthError
import empire.digiprem.kmptemplate.feature.auth.repository.IAuthRepository

class VerifyIdentityUseCase(private val repository: IAuthRepository) {
    suspend operator fun invoke(code: String): Result<Boolean?, AuthError> =
        repository.verifyIdentity(code)
            .mapFailure { dataError ->
                when (dataError) {
                    DataError.Remote.BadRequest   -> AuthError.InvalidCode
                    DataError.Remote.Unauthorized -> AuthError.CodeExpired
                    else                          -> AuthError.Unknown
                }
            }
}
