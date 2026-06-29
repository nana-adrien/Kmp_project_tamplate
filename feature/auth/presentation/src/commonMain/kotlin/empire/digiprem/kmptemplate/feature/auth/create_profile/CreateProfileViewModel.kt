package empire.digiprem.kmptemplate.feature.auth.create_profile

import androidx.lifecycle.viewModelScope
import empire.digiprem.kmptemplate.core.domain.util.onFailure
import empire.digiprem.kmptemplate.core.domain.util.onSuccess
import empire.digiprem.kmptemplate.core.presentation.extension.UiText
import empire.digiprem.kmptemplate.core.presentation.util.AbstractViewModel
import empire.digiprem.kmptemplate.feature.auth.usecase.CreateProfileUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateProfileViewModel(
    private val createProfileUseCase: CreateProfileUseCase,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(CreateProfileState())
    val state = combine(_state) { states ->
        val s = states[0]
        s.copy(
            isFormValid = s.firstName.isNotBlank()
                && s.lastName.isNotBlank()
                && s.username.isNotBlank(),
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), CreateProfileState())

    private val _events = Channel<CreateProfileEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: CreateProfileAction) {
        when (action) {
            is CreateProfileAction.OnFirstNameChange -> _state.update { it.copy(firstName = action.value, firstNameError = null) }
            is CreateProfileAction.OnLastNameChange  -> _state.update { it.copy(lastName = action.value, lastNameError = null) }
            is CreateProfileAction.OnUsernameChange  -> _state.update { it.copy(username = action.value, usernameError = null) }
            CreateProfileAction.OnSubmitClick        -> submit()
        }
    }

    private fun submit() {
        val current = _state.value
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            createProfileUseCase(current.firstName, current.lastName, current.username)
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    _events.send(CreateProfileEvent.OnProfileCreated)
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
}
