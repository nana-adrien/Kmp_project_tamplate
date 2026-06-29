package empire.digiprem.kmptemplate.feature.auth.forgot_password

import androidx.lifecycle.viewModelScope
import empire.digiprem.kmptemplate.core.domain.util.onFailure
import empire.digiprem.kmptemplate.core.domain.util.onSuccess
import empire.digiprem.kmptemplate.core.presentation.extension.UiText
import empire.digiprem.kmptemplate.core.presentation.util.AbstractViewModel
import empire.digiprem.kmptemplate.feature.auth.usecase.ForgotPasswordUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(ForgotPasswordState())
    val state = combine(_state) { states ->
        states[0].copy(
            isFormValid = states[0].email.isNotBlank() && states[0].email.contains("@"),
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), ForgotPasswordState())

    private val _events = Channel<ForgotPasswordEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: ForgotPasswordAction) {
        when (action) {
            is ForgotPasswordAction.OnEmailChange -> _state.update {
                it.copy(email = action.value, emailError = null)
            }
            ForgotPasswordAction.OnSubmitClick  -> submit()
            ForgotPasswordAction.OnNavigateBack -> viewModelScope.launch {
                _events.send(ForgotPasswordEvent.OnNavigateBack)
            }
        }
    }

    private fun submit() {
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            forgotPasswordUseCase(_state.value.email)
                .onSuccess {
                    _state.update { it.copy(isLoading = false, isSuccess = true) }
                    _events.send(ForgotPasswordEvent.OnSuccess)
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
