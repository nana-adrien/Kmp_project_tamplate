package empire.digiprem.kmptemplate.feature.settings.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.kmptemplate.core.design_system.components.buttons.AppButton
import empire.digiprem.kmptemplate.core.design_system.components.buttons.AppButtonType
import empire.digiprem.kmptemplate.core.design_system.components.topbar.AppTopBar
import empire.digiprem.kmptemplate.core.presentation.util.ObservableEvent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsRoot(
    onLogout : () -> Unit,
    viewModel: SettingsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObservableEvent(flow = viewModel.events) { event ->
        when (event) {
            SettingsEvent.OnLogout -> onLogout()
        }
    }

    SettingsScreen(state = state, onAction = viewModel::onAction)
}

@Composable
fun SettingsScreen(
    state   : SettingsState,
    onAction: (SettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar   = { AppTopBar(title = "Settings") },
    ) { padding ->
        Column(
            modifier            = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Top,
        ) {
            Spacer(Modifier.height(16.dp))

            SettingsToggleRow(
                label           = "Dark Theme",
                checked         = state.settings.isDarkTheme,
                onCheckedChange = { onAction(SettingsAction.OnDarkThemeToggle(it)) },
            )
            HorizontalDivider()

            SettingsToggleRow(
                label           = "Notifications",
                checked         = state.settings.notificationsEnabled,
                onCheckedChange = { onAction(SettingsAction.OnNotificationsToggle(it)) },
            )
            HorizontalDivider()

            Spacer(Modifier.height(8.dp))
            SettingsLabelRow(label = "Language")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AppButton(
                    text      = "EN",
                    onClick   = { onAction(SettingsAction.OnLanguageChange("en")) },
                    type      = if (state.settings.language == "en") AppButtonType.Primary else AppButtonType.Secondary,
                    fullWidth = false,
                )
                AppButton(
                    text      = "FR",
                    onClick   = { onAction(SettingsAction.OnLanguageChange("fr")) },
                    type      = if (state.settings.language == "fr") AppButtonType.Primary else AppButtonType.Secondary,
                    fullWidth = false,
                )
            }
            HorizontalDivider()

            Spacer(Modifier.height(32.dp))
            AppButton(
                text    = "Log Out",
                onClick = { onAction(SettingsAction.OnLogout) },
                type    = AppButtonType.Secondary,
            )
        }
    }
}

@Composable
private fun SettingsToggleRow(
    label          : String,
    checked        : Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier       : Modifier = Modifier,
) {
    Row(
        modifier              = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically,
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun SettingsLabelRow(
    label   : String,
    modifier: Modifier = Modifier,
) {
    Text(
        text     = label,
        style    = MaterialTheme.typography.bodyLarge,
        modifier = modifier.padding(vertical = 12.dp),
    )
}
