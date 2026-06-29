package empire.digiprem.kmptemplate.feature.settings.settings

sealed interface SettingsAction {
    data class OnDarkThemeToggle(val enabled: Boolean)     : SettingsAction
    data class OnLanguageChange(val language: String)      : SettingsAction
    data class OnNotificationsToggle(val enabled: Boolean) : SettingsAction
    data object OnLogout                                   : SettingsAction
}
