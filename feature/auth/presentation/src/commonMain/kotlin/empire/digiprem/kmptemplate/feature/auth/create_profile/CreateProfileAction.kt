package empire.digiprem.kmptemplate.feature.auth.create_profile

sealed interface CreateProfileAction {
    data class OnFirstNameChange(val value: String) : CreateProfileAction
    data class OnLastNameChange(val value: String)  : CreateProfileAction
    data class OnUsernameChange(val value: String)  : CreateProfileAction
    data object OnSubmitClick                       : CreateProfileAction
}
