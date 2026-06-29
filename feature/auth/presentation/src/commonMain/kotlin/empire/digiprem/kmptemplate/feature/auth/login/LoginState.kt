package empire.digiprem.kmptemplate.feature.auth.login

import empire.digiprem.kmptemplate.core.presentation.extension.UiText

data class LoginState(
    val email           : String  = "",
    val password        : String  = "",
    val isPasswordVisible: Boolean = false,
    val emailError      : UiText? = null,
    val passwordError   : UiText? = null,
    val errorMessage    : UiText? = null,
    val isLoading       : Boolean = false,
    val isFormValid     : Boolean = false,
)
