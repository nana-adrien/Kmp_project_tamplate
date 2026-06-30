package empire.digiprem.kmptemplate.contracts.notifications

import kotlinx.serialization.Serializable

@Serializable
data class NotificationDto(
    val id: String,
    val title: String,
    val body: String,
    val isRead: Boolean,
    val createdAt: String,
)
