package empire.digiprem.kmptemplate.feature.auth.login

sealed interface LoginAction {
    data class OnEmailChange(val value: String)    : LoginAction
    data class OnPasswordChange(val value: String) : LoginAction
    data object OnTogglePasswordVisibility         : LoginAction
    data object OnLoginClick                       : LoginAction
    data object OnForgotPasswordClick              : LoginAction
}
