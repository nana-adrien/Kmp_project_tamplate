package empire.digiprem.kmptemplate.feature.auth.reset_password

sealed interface ResetPasswordEvent {
    data object OnSuccess      : ResetPasswordEvent
    data object OnNavigateBack : ResetPasswordEvent
}
