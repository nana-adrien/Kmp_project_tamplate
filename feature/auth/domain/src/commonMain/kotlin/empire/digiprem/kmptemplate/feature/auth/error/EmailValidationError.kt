package empire.digiprem.kmptemplate.feature.auth.error

import empire.digiprem.kmptemplate.core.domain.util.ResultError

sealed interface EmailValidationError : ResultError {
    data object Empty         : EmailValidationError
    data object InvalidFormat : EmailValidationError
}
