package empire.digiprem.kmptemplate.feature.profile.profile

import androidx.lifecycle.viewModelScope
import empire.digiprem.kmptemplate.core.domain.util.onFailure
import empire.digiprem.kmptemplate.core.domain.util.onSuccess
import empire.digiprem.kmptemplate.core.presentation.extension.UiText
import empire.digiprem.kmptemplate.core.presentation.util.AbstractViewModel
import empire.digiprem.kmptemplate.feature.profile.model.Profile
import empire.digiprem.kmptemplate.feature.profile.usecase.GetProfileUseCase
import empire.digiprem.kmptemplate.feature.profile.usecase.UpdateProfileUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getProfileUseCase    : GetProfileUseCase,
    private val updateProfileUseCase : UpdateProfileUseCase,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = combine(_state) { states ->
        val s = states[0]
        s.copy(isFormValid = s.firstName.isNotBlank() && s.lastName.isNotBlank())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), ProfileState())

    private val _events = Channel<ProfileEvent>()
    val events = _events.receiveAsFlow()

    init {
        loadProfile()
    }

    fun onAction(action: ProfileAction) {
        when (action) {
            ProfileAction.OnLoadProfile      -> loadProfile()
            ProfileAction.OnToggleEditMode   -> _state.update { it.copy(isEditMode = !it.isEditMode) }
            is ProfileAction.OnFirstNameChange -> _state.update { it.copy(firstName = action.value) }
            is ProfileAction.OnLastNameChange  -> _state.update { it.copy(lastName = action.value) }
            is ProfileAction.OnUsernameChange  -> _state.update { it.copy(username = action.value) }
            is ProfileAction.OnBioChange       -> _state.update { it.copy(bio = action.value) }
            ProfileAction.OnSaveClick          -> save()
        }
    }

    private fun loadProfile() {
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            getProfileUseCase()
                .onSuccess { profile ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            profile   = profile,
                            firstName = profile?.firstName ?: "",
                            lastName  = profile?.lastName ?: "",
                            username  = profile?.username ?: "",
                            bio       = profile?.bio ?: "",
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading    = false,
                            errorMessage = UiText.DynamicString(error.toString()),
                        )
                    }
                }
        }
    }

    private fun save() {
        val current = _state.value
        val profile = current.profile ?: return
        _state.update { it.copy(isSaving = true, errorMessage = null) }
        viewModelScope.launch {
            updateProfileUseCase(
                profile.copy(
                    firstName = current.firstName,
                    lastName  = current.lastName,
                    username  = current.username,
                    bio       = current.bio.takeIf { it.isNotBlank() },
                )
            )
                .onSuccess { updated ->
                    _state.update {
                        it.copy(
                            isSaving   = false,
                            profile    = updated,
                            isEditMode = false,
                        )
                    }
                    _events.send(ProfileEvent.OnProfileUpdated)
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isSaving     = false,
                            errorMessage = UiText.DynamicString(error.toString()),
                        )
                    }
                }
        }
    }
}
