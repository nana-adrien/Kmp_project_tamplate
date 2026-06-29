package empire.digiprem.kmptemplate.feature.profile.profile

sealed interface ProfileAction {
    data object OnLoadProfile                        : ProfileAction
    data object OnToggleEditMode                     : ProfileAction
    data class OnFirstNameChange(val value: String)  : ProfileAction
    data class OnLastNameChange(val value: String)   : ProfileAction
    data class OnUsernameChange(val value: String)   : ProfileAction
    data class OnBioChange(val value: String)        : ProfileAction
    data object OnSaveClick                          : ProfileAction
}
