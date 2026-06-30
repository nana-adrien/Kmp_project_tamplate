package empire.digiprem.kmptemplate.contracts.notifications

import kotlinx.serialization.Serializable

@Serializable
data class MarkNotificationsReadRequest(
    val ids: List<String>,
)
