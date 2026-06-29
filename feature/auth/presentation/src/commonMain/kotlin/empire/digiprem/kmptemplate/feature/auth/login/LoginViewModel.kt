package empire.digiprem.kmptemplate.feature.auth.login

import androidx.lifecycle.viewModelScope
import empire.digiprem.kmptemplate.core.domain.util.onFailure
import empire.digiprem.kmptemplate.core.domain.util.onSuccess
import empire.digiprem.kmptemplate.core.presentation.extension.UiText
import empire.digiprem.kmptemplate.core.presentation.util.AbstractViewModel
import empire.digiprem.kmptemplate.feature.auth.usecase.LoginUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = combine(_state) { states -> states[0].copy(
        isFormValid = states[0].email.isNotBlank() && states[0].password.length >= 6
    ) }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), LoginState())

    private val _events = Channel<LoginEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnEmailChange    -> _state.update { it.copy(email = action.value, emailError = null) }
            is LoginAction.OnPasswordChange -> _state.update { it.copy(password = action.value, passwordError = null) }
            LoginAction.OnTogglePasswordVisibility -> _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            LoginAction.OnLoginClick        -> login()
            LoginAction.OnForgotPasswordClick -> viewModelScope.launch { _events.send(LoginEvent.OnNavigateToForgot) }
        }
    }

    private fun login() {
        val current = _state.value
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            loginUseCase(current.email, current.password)
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    _events.send(LoginEvent.OnLoginSuccess)
                }
                .onFailure { error ->
                    _state.update { it.copy(
                        isLoading    = false,
                        errorMessage = UiText.DynamicString(error.toString()),
                    ) }
                }
        }
    }
}
