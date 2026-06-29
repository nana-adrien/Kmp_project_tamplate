package empire.digiprem.kmptemplate.feature.dashboard.dashboard

import androidx.lifecycle.viewModelScope
import empire.digiprem.kmptemplate.core.presentation.util.AbstractViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel : AbstractViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), DashboardState())

    private val _events = Channel<DashboardEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: DashboardAction) {
        when (action) {
            is DashboardAction.OnDestinationChange ->
                _state.update { it.copy(currentDestination = action.destination) }
        }
    }

    fun onLogout() {
        viewModelScope.launch { _events.send(DashboardEvent.OnLogout) }
    }
}
