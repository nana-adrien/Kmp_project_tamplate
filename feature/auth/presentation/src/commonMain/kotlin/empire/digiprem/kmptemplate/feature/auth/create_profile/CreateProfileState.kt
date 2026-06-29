package empire.digiprem.kmptemplate.feature.auth.create_profile

import empire.digiprem.kmptemplate.core.presentation.extension.UiText

data class CreateProfileState(
    val firstName      : String  = "",
    val lastName       : String  = "",
    val username       : String  = "",
    val firstNameError : UiText? = null,
    val lastNameError  : UiText? = null,
    val usernameError  : UiText? = null,
    val errorMessage   : UiText? = null,
    val isLoading      : Boolean = false,
    val isFormValid    : Boolean = false,
)
