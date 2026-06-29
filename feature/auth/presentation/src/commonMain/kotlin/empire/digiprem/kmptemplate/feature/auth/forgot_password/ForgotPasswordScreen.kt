package empire.digiprem.kmptemplate.feature.auth.forgot_password

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.kmptemplate.core.design_system.components.buttons.AppButton
import empire.digiprem.kmptemplate.core.design_system.components.textfields.AppTextField
import empire.digiprem.kmptemplate.core.design_system.extension.asString
import empire.digiprem.kmptemplate.core.presentation.util.ObservableEvent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ForgotPasswordRoot(
    onSuccess      : () -> Unit,
    onNavigateBack : () -> Unit,
    viewModel      : ForgotPasswordViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObservableEvent(flow = viewModel.events) { event ->
        when (event) {
            ForgotPasswordEvent.OnSuccess      -> onSuccess()
            ForgotPasswordEvent.OnNavigateBack -> onNavigateBack()
        }
    }

    ForgotPasswordScreen(state = state, onAction = viewModel::onAction)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    state   : ForgotPasswordState,
    onAction: (ForgotPasswordAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar   = {
            TopAppBar(
                title = { Text("Forgot Password") },
                navigationIcon = {
                    IconButton(onClick = { onAction(ForgotPasswordAction.OnNavigateBack) }) {
                        Icon(
                            imageVector        = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier            = Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Top,
        ) {
            Spacer(Modifier.height(16.dp))
            Text(
                text  = "Enter your email and we'll send you a reset link.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(24.dp))

            AppTextField(
                value          = state.email,
                onValueChange  = { onAction(ForgotPasswordAction.OnEmailChange(it)) },
                label          = "Email",
                placeholder    = "example@email.com",
                supportingText = state.emailError?.asString(),
                isError        = state.emailError != null,
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

            if (state.isSuccess) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text  = "Check your email! A reset link has been sent.",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            Spacer(Modifier.height(24.dp))
            AppButton(
                text      = "Send Reset Link",
                onClick   = { onAction(ForgotPasswordAction.OnSubmitClick) },
                enabled   = state.isFormValid,
                isLoading = state.isLoading,
            )
        }
    }
}
