package empire.digiprem.kmptemplate.feature.auth.error

import empire.digiprem.kmptemplate.core.domain.util.ResultError

sealed interface AuthError : ResultError {
    data object InvalidCredentials  : AuthError
    data object AccountLocked       : AuthError
    data object AccountNotVerified  : AuthError
    data object SessionExpired      : AuthError
    data object EmailAlreadyExists  : AuthError
    data object WeakPassword        : AuthError
    data object InvalidCode         : AuthError
    data object CodeExpired         : AuthError
    data object Unknown             : AuthError
}
