package empire.digiprem.kmptemplate.feature.auth.login

sealed interface LoginEvent {
    data object OnLoginSuccess      : LoginEvent
    data object OnNavigateToForgot  : LoginEvent
}
