package empire.digiprem.kmptemplate.feature.notifications.notifications

sealed interface NotificationsEvent {
    data object OnError : NotificationsEvent
}
