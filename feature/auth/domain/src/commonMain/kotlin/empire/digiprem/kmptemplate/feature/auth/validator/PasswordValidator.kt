package empire.digiprem.kmptemplate.feature.auth.validator

import empire.digiprem.kmptemplate.core.domain.util.Result
import empire.digiprem.kmptemplate.feature.auth.error.PasswordValidationError

object PasswordValidator {
    private const val MIN_LENGTH = 8

    fun validate(password: String): Result<Unit, PasswordValidationError> = when {
        password.isBlank() -> Result.Failure(PasswordValidationError.Empty)
        password.length < MIN_LENGTH -> Result.Failure(PasswordValidationError.TooShort)
        !password.any { it.isDigit() } -> Result.Failure(PasswordValidationError.NoDigit)
        else -> Result.Success(Unit)
    }
}
