package empire.digiprem.kmptemplate.feature.auth.error

import empire.digiprem.kmptemplate.core.domain.util.ResultError

sealed interface PasswordValidationError : ResultError {
    data object Empty    : PasswordValidationError
    data object TooShort : PasswordValidationError
    data object NoDigit  : PasswordValidationError
}
