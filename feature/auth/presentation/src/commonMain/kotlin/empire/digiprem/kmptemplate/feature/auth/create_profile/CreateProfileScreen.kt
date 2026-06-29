package empire.digiprem.kmptemplate.feature.auth.create_profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.kmptemplate.core.design_system.components.buttons.AppButton
import empire.digiprem.kmptemplate.core.design_system.components.textfields.AppTextField
import empire.digiprem.kmptemplate.core.design_system.extension.asString
import empire.digiprem.kmptemplate.core.presentation.util.ObservableEvent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreateProfileRoot(
    onProfileCreated: () -> Unit,
    viewModel       : CreateProfileViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObservableEvent(flow = viewModel.events) { event ->
        when (event) {
            CreateProfileEvent.OnProfileCreated -> onProfileCreated()
        }
    }

    CreateProfileScreen(state = state, onAction = viewModel::onAction)
}

@Composable
fun CreateProfileScreen(
    state   : CreateProfileState,
    onAction: (CreateProfileAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier) { padding ->
        Column(
            modifier            = Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "Complete Your Profile", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(8.dp))
            Text(
                text  = "Tell us a bit about yourself.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(32.dp))

            AppTextField(
                value          = state.firstName,
                onValueChange  = { onAction(CreateProfileAction.OnFirstNameChange(it)) },
                label          = "First Name",
                supportingText = state.firstNameError?.asString(),
                isError        = state.firstNameError != null,
                modifier       = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(16.dp))

            AppTextField(
                value          = state.lastName,
                onValueChange  = { onAction(CreateProfileAction.OnLastNameChange(it)) },
                label          = "Last Name",
                supportingText = state.lastNameError?.asString(),
                isError        = state.lastNameError != null,
                modifier       = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(16.dp))

            AppTextField(
                value          = state.username,
                onValueChange  = { onAction(CreateProfileAction.OnUsernameChange(it)) },
                label          = "Username",
                placeholder    = "@username",
                supportingText = state.usernameError?.asString(),
                isError        = state.usernameError != null,
                modifier       = Modifier.fillMaxWidth(),
            )

            state.errorMessage?.let {
                Spacer(Modifier.height(8.dp))
                Text(
                    text  = it.asString(),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            Spacer(Modifier.height(24.dp))
            AppButton(
                text      = "Get Started",
                onClick   = { onAction(CreateProfileAction.OnSubmitClick) },
                enabled   = state.isFormValid,
                isLoading = state.isLoading,
            )
        }
    }
}
