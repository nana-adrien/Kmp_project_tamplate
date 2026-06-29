package empire.digiprem.kmptemplate.feature.notifications.dto

import kotlinx.serialization.Serializable

@Serializable
data class NotificationDto(
    val id        : String,
    val title     : String,
    val body      : String,
    val isRead    : Boolean = false,
    val createdAt : Long    = 0L,
    val type      : String  = "",
)
