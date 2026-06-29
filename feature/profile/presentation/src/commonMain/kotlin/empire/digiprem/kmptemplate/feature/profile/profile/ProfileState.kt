package empire.digiprem.kmptemplate.feature.profile.profile

import empire.digiprem.kmptemplate.core.presentation.extension.UiText
import empire.digiprem.kmptemplate.feature.profile.model.Profile

data class ProfileState(
    val profile      : Profile? = null,
    val firstName    : String   = "",
    val lastName     : String   = "",
    val username     : String   = "",
    val bio          : String   = "",
    val isLoading    : Boolean  = false,
    val isSaving     : Boolean  = false,
    val isEditMode   : Boolean  = false,
    val errorMessage : UiText?  = null,
    val isFormValid  : Boolean  = false,
)
