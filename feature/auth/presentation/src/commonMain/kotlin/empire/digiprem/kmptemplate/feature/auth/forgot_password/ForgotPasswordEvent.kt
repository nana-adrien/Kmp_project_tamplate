package empire.digiprem.kmptemplate.feature.auth.forgot_password

sealed interface ForgotPasswordEvent {
    data object OnSuccess      : ForgotPasswordEvent
    data object OnNavigateBack : ForgotPasswordEvent
}
