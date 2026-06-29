package empire.digiprem.kmptemplate.feature.auth.usecase

import empire.digiprem.kmptemplate.core.domain.network.DataError
import empire.digiprem.kmptemplate.core.domain.util.Result
import empire.digiprem.kmptemplate.core.domain.util.mapFailure
import empire.digiprem.kmptemplate.feature.auth.error.AuthError
import empire.digiprem.kmptemplate.feature.auth.repository.IAuthRepository

class ResetPasswordUseCase(private val repository: IAuthRepository) {
    suspend operator fun invoke(
        token       : String,
        newPassword : String,
    ): Result<Unit?, AuthError> =
        repository.resetPassword(token, newPassword)
            .mapFailure { dataError ->
                when (dataError) {
                    DataError.Remote.Unauthorized -> AuthError.CodeExpired
                    DataError.Remote.BadRequest   -> AuthError.WeakPassword
                    else                          -> AuthError.Unknown
                }
            }
}
