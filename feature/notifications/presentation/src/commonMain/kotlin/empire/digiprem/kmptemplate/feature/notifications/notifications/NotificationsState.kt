package empire.digiprem.kmptemplate.feature.notifications.notifications

import empire.digiprem.kmptemplate.core.presentation.extension.UiText
import empire.digiprem.kmptemplate.feature.notifications.model.Notification

data class NotificationsState(
    val notifications : List<Notification> = emptyList(),
    val isLoading     : Boolean            = false,
    val errorMessage  : UiText?            = null,
)
