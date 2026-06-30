package empire.digiprem.kmptemplate.contracts.settings

import kotlinx.serialization.Serializable

@Serializable
data class UpdateSettingsRequest(
    val language: String? = null,
    val theme: String? = null,
    val notificationsEnabled: Boolean? = null,
)
