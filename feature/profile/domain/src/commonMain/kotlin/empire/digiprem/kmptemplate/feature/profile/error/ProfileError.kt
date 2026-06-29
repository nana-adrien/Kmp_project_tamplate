package empire.digiprem.kmptemplate.feature.profile.error

import empire.digiprem.kmptemplate.core.domain.util.ResultError

sealed interface ProfileError : ResultError {
    data object NotFound  : ProfileError
    data object Forbidden : ProfileError
    data object Unknown   : ProfileError
}
