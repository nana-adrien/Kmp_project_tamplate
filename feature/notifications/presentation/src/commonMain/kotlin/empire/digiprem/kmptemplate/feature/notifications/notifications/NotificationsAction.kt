package empire.digiprem.kmptemplate.feature.notifications.notifications

sealed interface NotificationsAction {
    data object OnRefresh                             : NotificationsAction
    data class OnMarkAsRead(val id: String)           : NotificationsAction
    data class OnDeleteNotification(val id: String)   : NotificationsAction
}
