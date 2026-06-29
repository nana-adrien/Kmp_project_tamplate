package empire.digiprem.kmptemplate.feature.settings.settings

sealed interface SettingsEvent {
    data object OnLogout : SettingsEvent
}
