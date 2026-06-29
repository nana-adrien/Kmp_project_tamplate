package empire.digiprem.kmptemplate.feature.dashboard.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.kmptemplate.core.presentation.util.ObservableEvent
import empire.digiprem.kmptemplate.feature.notifications.notifications.NotificationsRoot
import empire.digiprem.kmptemplate.feature.profile.profile.ProfileRoot
import empire.digiprem.kmptemplate.feature.settings.settings.SettingsRoot
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DashboardRoot(
    onLogout : () -> Unit,
    viewModel: DashboardViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObservableEvent(flow = viewModel.events) { event ->
        when (event) {
            DashboardEvent.OnLogout -> onLogout()
        }
    }

    DashboardScreen(
        state    = state,
        onAction = viewModel::onAction,
        onLogout = viewModel::onLogout,
    )
}

@Composable
fun DashboardScreen(
    state   : DashboardState,
    onAction: (DashboardAction) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val windowWidth = LocalWindowInfo.current.containerSize.width
    val isCompact   = windowWidth < 600f

    if (isCompact) {
        Scaffold(
            modifier  = modifier,
            bottomBar = {
                NavigationBar {
                    DashboardDestination.entries.forEach { dest ->
                        NavigationBarItem(
                            selected = state.currentDestination == dest,
                            onClick  = { onAction(DashboardAction.OnDestinationChange(dest)) },
                            icon     = { Icon(dest.icon, contentDescription = dest.label) },
                            label    = { Text(dest.label) },
                        )
                    }
                }
            },
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                DashboardContent(
                    destination = state.currentDestination,
                    onLogout    = onLogout,
                )
            }
        }
    } else {
        Row(modifier = modifier.fillMaxSize()) {
            NavigationRail {
                DashboardDestination.entries.forEach { dest ->
                    NavigationRailItem(
                        selected = state.currentDestination == dest,
                        onClick  = { onAction(DashboardAction.OnDestinationChange(dest)) },
                        icon     = { Icon(dest.icon, contentDescription = dest.label) },
                        label    = { Text(dest.label) },
                    )
                }
            }
            Box(modifier = Modifier.fillMaxSize()) {
                DashboardContent(
                    destination = state.currentDestination,
                    onLogout    = onLogout,
                )
            }
        }
    }
}

@Composable
private fun DashboardContent(
    destination: DashboardDestination,
    onLogout   : () -> Unit,
) {
    when (destination) {
        DashboardDestination.Profile       -> ProfileRoot()
        DashboardDestination.Notifications -> NotificationsRoot()
        DashboardDestination.Settings      -> SettingsRoot(onLogout = onLogout)
    }
}
