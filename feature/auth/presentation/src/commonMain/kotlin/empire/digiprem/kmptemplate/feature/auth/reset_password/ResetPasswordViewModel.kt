package empire.digiprem.kmptemplate.feature.auth.reset_password

import androidx.lifecycle.viewModelScope
import empire.digiprem.kmptemplate.core.domain.util.onFailure
import empire.digiprem.kmptemplate.core.domain.util.onSuccess
import empire.digiprem.kmptemplate.core.presentation.extension.UiText
import empire.digiprem.kmptemplate.core.presentation.util.AbstractViewModel
import empire.digiprem.kmptemplate.feature.auth.usecase.ResetPasswordUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResetPasswordViewModel(
    private val resetPasswordUseCase: ResetPasswordUseCase,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(ResetPasswordState())
    val state = combine(_state) { states ->
        val s = states[0]
        s.copy(
            isFormValid = s.code.isNotBlank()
                && s.newPassword.length >= 6
                && s.confirmPassword == s.newPassword,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), ResetPasswordState())

    private val _events = Channel<ResetPasswordEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: ResetPasswordAction) {
        when (action) {
            is ResetPasswordAction.OnCodeChange            -> _state.update { it.copy(code = action.value, codeError = null) }
            is ResetPasswordAction.OnNewPasswordChange     -> _state.update { it.copy(newPassword = action.value, newPasswordError = null) }
            is ResetPasswordAction.OnConfirmPasswordChange -> _state.update { it.copy(confirmPassword = action.value, confirmError = null) }
            ResetPasswordAction.OnToggleNewPasswordVisibility     -> _state.update { it.copy(isNewPasswordVisible = !it.isNewPasswordVisible) }
            ResetPasswordAction.OnToggleConfirmPasswordVisibility -> _state.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
            ResetPasswordAction.OnSubmitClick  -> submit()
            ResetPasswordAction.OnNavigateBack -> viewModelScope.launch {
                _events.send(ResetPasswordEvent.OnNavigateBack)
            }
        }
    }

    private fun submit() {
        val current = _state.value
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            resetPasswordUseCase(current.code, current.newPassword)
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    _events.send(ResetPasswordEvent.OnSuccess)
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
