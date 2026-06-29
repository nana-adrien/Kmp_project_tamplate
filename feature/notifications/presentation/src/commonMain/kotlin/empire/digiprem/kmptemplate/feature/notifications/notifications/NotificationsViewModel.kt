package empire.digiprem.kmptemplate.feature.notifications.notifications

import androidx.lifecycle.viewModelScope
import empire.digiprem.kmptemplate.core.domain.util.onFailure
import empire.digiprem.kmptemplate.core.domain.util.onSuccess
import empire.digiprem.kmptemplate.core.presentation.extension.UiText
import empire.digiprem.kmptemplate.core.presentation.util.AbstractViewModel
import empire.digiprem.kmptemplate.feature.notifications.usecase.DeleteNotificationUseCase
import empire.digiprem.kmptemplate.feature.notifications.usecase.GetNotificationsUseCase
import empire.digiprem.kmptemplate.feature.notifications.usecase.MarkAsReadUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotificationsViewModel(
    private val getNotificationsUseCase  : GetNotificationsUseCase,
    private val markAsReadUseCase        : MarkAsReadUseCase,
    private val deleteNotificationUseCase: DeleteNotificationUseCase,
) : AbstractViewModel() {

    private val _state = MutableStateFlow(NotificationsState())
    val state = _state.asStateFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), NotificationsState())

    private val _events = Channel<NotificationsEvent>()
    val events = _events.receiveAsFlow()

    init {
        loadNotifications()
    }

    fun onAction(action: NotificationsAction) {
        when (action) {
            NotificationsAction.OnRefresh                  -> loadNotifications()
            is NotificationsAction.OnMarkAsRead           -> markAsRead(action.id)
            is NotificationsAction.OnDeleteNotification   -> delete(action.id)
        }
    }

    private fun loadNotifications() {
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            getNotificationsUseCase()
                .onSuccess { list ->
                    _state.update { it.copy(isLoading = false, notifications = list ?: emptyList()) }
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

    private fun markAsRead(id: String) {
        viewModelScope.launch {
            markAsReadUseCase(id).onSuccess {
                _state.update { s ->
                    s.copy(
                        notifications = s.notifications.map { n ->
                            if (n.id == id) n.copy(isRead = true) else n
                        },
                    )
                }
            }
        }
    }

    private fun delete(id: String) {
        viewModelScope.launch {
            deleteNotificationUseCase(id).onSuccess {
                _state.update { s ->
                    s.copy(notifications = s.notifications.filter { it.id != id })
                }
            }
        }
    }
}
