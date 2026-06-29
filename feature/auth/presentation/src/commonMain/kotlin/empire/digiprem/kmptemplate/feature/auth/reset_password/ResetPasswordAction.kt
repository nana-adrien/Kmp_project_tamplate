package empire.digiprem.kmptemplate.feature.auth.reset_password

sealed interface ResetPasswordAction {
    data class OnCodeChange(val value: String)            : ResetPasswordAction
    data class OnNewPasswordChange(val value: String)     : ResetPasswordAction
    data class OnConfirmPasswordChange(val value: String) : ResetPasswordAction
    data object OnToggleNewPasswordVisibility             : ResetPasswordAction
    data object OnToggleConfirmPasswordVisibility         : ResetPasswordAction
    data object OnSubmitClick                             : ResetPasswordAction
    data object OnNavigateBack                            : ResetPasswordAction
}
