package empire.digiprem.kmptemplate.feature.auth.verify_identity

import empire.digiprem.kmptemplate.core.presentation.extension.UiText

data class VerifyIdentityState(
    val code         : String  = "",
    val codeError    : UiText? = null,
    val errorMessage : UiText? = null,
    val isLoading    : Boolean = false,
    val isFormValid  : Boolean = false,
)
