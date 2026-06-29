package empire.digiprem.kmptemplate.feature.auth.verify_identity

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.kmptemplate.core.design_system.components.buttons.AppButton
import empire.digiprem.kmptemplate.core.design_system.components.buttons.AppButtonType
import empire.digiprem.kmptemplate.core.design_system.components.textfields.AppOtpTextField
import empire.digiprem.kmptemplate.core.design_system.extension.asString
import empire.digiprem.kmptemplate.core.presentation.util.ObservableEvent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun VerifyIdentityRoot(
    onVerified     : () -> Unit,
    onNavigateBack : () -> Unit,
    viewModel      : VerifyIdentityViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObservableEvent(flow = viewModel.events) { event ->
        when (event) {
            VerifyIdentityEvent.OnVerified     -> onVerified()
            VerifyIdentityEvent.OnNavigateBack -> onNavigateBack()
        }
    }

    VerifyIdentityScreen(state = state, onAction = viewModel::onAction)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyIdentityScreen(
    state   : VerifyIdentityState,
    onAction: (VerifyIdentityAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar   = {
            TopAppBar(
                title = { Text("Verify Identity") },
                navigationIcon = {
                    IconButton(onClick = { onAction(VerifyIdentityAction.OnNavigateBack) }) {
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
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(16.dp))
            Text(
                text  = "Enter the 6-digit code sent to your email.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(32.dp))

            AppOtpTextField(
                value         = state.code,
                onValueChange = { onAction(VerifyIdentityAction.OnCodeChange(it)) },
                otpLength     = 6,
                isError       = state.codeError != null,
                modifier      = Modifier.fillMaxWidth(),
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
                text      = "Verify",
                onClick   = { onAction(VerifyIdentityAction.OnVerifyClick) },
                enabled   = state.isFormValid,
                isLoading = state.isLoading,
            )

            Spacer(Modifier.height(8.dp))
            AppButton(
                text      = "Resend Code",
                onClick   = { onAction(VerifyIdentityAction.OnResendClick) },
                type      = AppButtonType.Text,
                fullWidth = false,
            )
        }
    }
}
