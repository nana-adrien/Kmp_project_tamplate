package empire.digiprem.kmptemplate.feature.settings.settings

import empire.digiprem.kmptemplate.feature.settings.model.AppSettings

data class SettingsState(
    val settings  : AppSettings = AppSettings(),
    val isLoading : Boolean     = false,
)
