package empire.digiprem.kmptemplate.feature.auth.verify_identity

sealed interface VerifyIdentityEvent {
    data object OnVerified     : VerifyIdentityEvent
    data object OnNavigateBack : VerifyIdentityEvent
}
