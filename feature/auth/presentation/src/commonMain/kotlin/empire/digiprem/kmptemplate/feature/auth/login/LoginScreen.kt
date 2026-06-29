package empire.digiprem.kmptemplate.feature.auth.login

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
import empire.digiprem.kmptemplate.core.design_system.components.textfields.AppPasswordTextField
import empire.digiprem.kmptemplate.core.design_system.components.textfields.AppTextField
import empire.digiprem.kmptemplate.core.design_system.extension.asString
import empire.digiprem.kmptemplate.core.presentation.util.ObservableEvent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginRoot(
    onLoginSuccess   : () -> Unit,
    onForgotPassword : () -> Unit,
    viewModel        : LoginViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObservableEvent(flow = viewModel.events) { event ->
        when (event) {
            LoginEvent.OnLoginSuccess     -> onLoginSuccess()
            LoginEvent.OnNavigateToForgot -> onForgotPassword()
        }
    }

    LoginScreen(state = state, onAction = viewModel::onAction)
}

@Composable
fun LoginScreen(
    state   : LoginState,
    onAction: (LoginAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier) { padding ->
        Column(
            modifier            = Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "Sign In", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(32.dp))

            AppTextField(
                value         = state.email,
                onValueChange = { onAction(LoginAction.OnEmailChange(it)) },
                label         = "Email",
                placeholder   = "example@email.com",
                supportingText = state.emailError?.asString(),
                isError       = state.emailError != null,
                modifier      = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(16.dp))

            AppPasswordTextField(
                value              = state.password,
                onValueChange      = { onAction(LoginAction.OnPasswordChange(it)) },
                isPasswordVisible  = state.isPasswordVisible,
                onToggleVisibility = { onAction(LoginAction.OnTogglePasswordVisibility) },
                label              = "Password",
                supportingText     = state.passwordError?.asString(),
                isError            = state.passwordError != null,
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

            Spacer(Modifier.height(8.dp))
            AppButton(
                text      = "Forgot Password?",
                onClick   = { onAction(LoginAction.OnForgotPasswordClick) },
                type      = empire.digiprem.kmptemplate.core.design_system.components.buttons.AppButtonType.Text,
                fullWidth = false,
            )

            Spacer(Modifier.height(24.dp))
            AppButton(
                text      = "Sign In",
                onClick   = { onAction(LoginAction.OnLoginClick) },
                isLoading = state.isLoading,
                enabled   = state.isFormValid,
            )
        }
    }
}
