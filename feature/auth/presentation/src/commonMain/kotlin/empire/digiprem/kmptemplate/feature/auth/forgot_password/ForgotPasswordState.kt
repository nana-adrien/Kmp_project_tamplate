package empire.digiprem.kmptemplate.feature.auth.forgot_password

import empire.digiprem.kmptemplate.core.presentation.extension.UiText

data class ForgotPasswordState(
    val email        : String  = "",
    val emailError   : UiText? = null,
    val errorMessage : UiText? = null,
    val isLoading    : Boolean = false,
    val isSuccess    : Boolean = false,
    val isFormValid  : Boolean = false,
)
