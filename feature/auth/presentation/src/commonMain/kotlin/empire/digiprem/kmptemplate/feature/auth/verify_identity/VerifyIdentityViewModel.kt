package empire.digiprem.kmptemplate.feature.auth.verify_identity

import androidx.lifecycle.viewModelScope
import empire.digiprem.kmptemplate.core.domain.util.onFailure
import empire.digiprem.kmptemplate.core.domain.util.onSuccess
import empire.digiprem.kmptemplate.core.presentation.extension.UiText
import empire.digiprem.kmptemplate.core.presentation.util.AbstractViewModel
import empire.digiprem.kmptemplate.feature.auth.usecase.VerifyIdentityUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VerifyIdentityViewModel(
    private val verifyIdentityUseCase: VerifyIdentityUseCase,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(VerifyIdentityState())
    val state = combine(_state) { states ->
        states[0].copy(isFormValid = states[0].code.length == 6)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), VerifyIdentityState())

    private val _events = Channel<VerifyIdentityEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: VerifyIdentityAction) {
        when (action) {
            is VerifyIdentityAction.OnCodeChange  -> _state.update { it.copy(code = action.value, codeError = null) }
            VerifyIdentityAction.OnVerifyClick    -> verify()
            VerifyIdentityAction.OnResendClick    -> _state.update { it.copy(code = "", codeError = null, errorMessage = null) }
            VerifyIdentityAction.OnNavigateBack   -> viewModelScope.launch {
                _events.send(VerifyIdentityEvent.OnNavigateBack)
            }
        }
    }

    private fun verify() {
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            verifyIdentityUseCase(_state.value.code)
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    _events.send(VerifyIdentityEvent.OnVerified)
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
