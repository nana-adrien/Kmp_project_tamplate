package empire.digiprem.kmptemplate.feature.auth.verify_identity

sealed interface VerifyIdentityAction {
    data class OnCodeChange(val value: String) : VerifyIdentityAction
    data object OnVerifyClick                  : VerifyIdentityAction
    data object OnResendClick                  : VerifyIdentityAction
    data object OnNavigateBack                 : VerifyIdentityAction
}
