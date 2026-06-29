package empire.digiprem.kmptemplate.feature.settings.model

data class AppSettings(
    val isDarkTheme           : Boolean = false,
    val language              : String  = "en",
    val notificationsEnabled  : Boolean = true,
)
