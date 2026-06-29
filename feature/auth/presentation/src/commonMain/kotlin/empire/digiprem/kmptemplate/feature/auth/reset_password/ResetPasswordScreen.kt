package empire.digiprem.kmptemplate.feature.auth.reset_password

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
import empire.digiprem.kmptemplate.core.design_system.components.textfields.AppOtpTextField
import empire.digiprem.kmptemplate.core.design_system.components.textfields.AppPasswordTextField
import empire.digiprem.kmptemplate.core.design_system.extension.asString
import empire.digiprem.kmptemplate.core.presentation.util.ObservableEvent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ResetPasswordRoot(
    onSuccess      : () -> Unit,
    onNavigateBack : () -> Unit,
    viewModel      : ResetPasswordViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObservableEvent(flow = viewModel.events) { event ->
        when (event) {
            ResetPasswordEvent.OnSuccess      -> onSuccess()
            ResetPasswordEvent.OnNavigateBack -> onNavigateBack()
        }
    }

    ResetPasswordScreen(state = state, onAction = viewModel::onAction)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(
    state   : ResetPasswordState,
    onAction: (ResetPasswordAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar   = {
            TopAppBar(
                title = { Text("Reset Password") },
                navigationIcon = {
                    IconButton(onClick = { onAction(ResetPasswordAction.OnNavigateBack) }) {
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
                text  = "Enter the code from your email and your new password.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(24.dp))

            AppOtpTextField(
                value         = state.code,
                onValueChange = { onAction(ResetPasswordAction.OnCodeChange(it)) },
                otpLength     = 6,
                isError       = state.codeError != null,
                modifier      = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(16.dp))
            AppPasswordTextField(
                value              = state.newPassword,
                onValueChange      = { onAction(ResetPasswordAction.OnNewPasswordChange(it)) },
                isPasswordVisible  = state.isNewPasswordVisible,
                onToggleVisibility = { onAction(ResetPasswordAction.OnToggleNewPasswordVisibility) },
                label              = "New Password",
                supportingText     = state.newPasswordError?.asString(),
                isError            = state.newPasswordError != null,
                modifier           = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(16.dp))
            AppPasswordTextField(
                value              = state.confirmPassword,
                onValueChange      = { onAction(ResetPasswordAction.OnConfirmPasswordChange(it)) },
                isPasswordVisible  = state.isConfirmPasswordVisible,
                onToggleVisibility = { onAction(ResetPasswordAction.OnToggleConfirmPasswordVisibility) },
                label              = "Confirm Password",
                supportingText     = state.confirmError?.asString(),
                isError            = state.confirmError != null,
                modifier           = Modifier.fillMaxWidth(),
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
                text      = "Reset Password",
                onClick   = { onAction(ResetPasswordAction.OnSubmitClick) },
                enabled   = state.isFormValid,
                isLoading = state.isLoading,
            )
        }
    }
}
