package empire.digiprem.kmptemplate.contracts.settings

import kotlinx.serialization.Serializable

@Serializable
data class SettingsDto(
    val language: String,
    val theme: String,
    val notificationsEnabled: Boolean,
)
