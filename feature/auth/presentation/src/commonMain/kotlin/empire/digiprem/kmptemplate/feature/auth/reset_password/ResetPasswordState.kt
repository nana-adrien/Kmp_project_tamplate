package empire.digiprem.kmptemplate.feature.auth.reset_password

import empire.digiprem.kmptemplate.core.presentation.extension.UiText

data class ResetPasswordState(
    val code                     : String  = "",
    val newPassword              : String  = "",
    val confirmPassword          : String  = "",
    val isNewPasswordVisible     : Boolean = false,
    val isConfirmPasswordVisible : Boolean = false,
    val codeError                : UiText? = null,
    val newPasswordError         : UiText? = null,
    val confirmError             : UiText? = null,
    val errorMessage             : UiText? = null,
    val isLoading                : Boolean = false,
    val isFormValid              : Boolean = false,
)
