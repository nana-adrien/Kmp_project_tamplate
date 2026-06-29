package empire.digiprem.kmptemplate.feature.notifications.error

import empire.digiprem.kmptemplate.core.domain.util.ResultError

sealed interface NotificationError : ResultError {
    data object NotFound : NotificationError
    data object Unknown  : NotificationError
}
