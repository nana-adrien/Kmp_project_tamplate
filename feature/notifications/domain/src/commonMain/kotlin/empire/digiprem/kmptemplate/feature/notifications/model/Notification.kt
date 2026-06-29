package empire.digiprem.kmptemplate.feature.notifications.model

data class Notification(
    val id        : String,
    val title     : String,
    val body      : String,
    val isRead    : Boolean = false,
    val createdAt : Long    = 0L,
    val type      : String  = "",
)
