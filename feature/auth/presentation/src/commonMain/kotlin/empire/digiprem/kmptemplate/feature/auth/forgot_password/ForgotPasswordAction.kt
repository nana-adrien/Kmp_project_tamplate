package empire.digiprem.kmptemplate.feature.auth.forgot_password

sealed interface ForgotPasswordAction {
    data class OnEmailChange(val value: String) : ForgotPasswordAction
    data object OnSubmitClick                   : ForgotPasswordAction
    data object OnNavigateBack                  : ForgotPasswordAction
}
