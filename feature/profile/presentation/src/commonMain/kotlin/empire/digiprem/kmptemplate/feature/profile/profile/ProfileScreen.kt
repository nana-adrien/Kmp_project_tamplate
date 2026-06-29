package empire.digiprem.kmptemplate.feature.profile.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.kmptemplate.core.design_system.components.buttons.AppButton
import empire.digiprem.kmptemplate.core.design_system.components.buttons.AppButtonType
import empire.digiprem.kmptemplate.core.design_system.components.image.AppImage
import empire.digiprem.kmptemplate.core.design_system.components.image.AppImageSource
import empire.digiprem.kmptemplate.core.design_system.components.textfields.AppTextField
import empire.digiprem.kmptemplate.core.design_system.components.topbar.AppTopBar
import empire.digiprem.kmptemplate.core.design_system.extension.asString
import empire.digiprem.kmptemplate.core.presentation.util.ObservableEvent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileRoot(
    viewModel: ProfileViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ObservableEvent(flow = viewModel.events) { /* no navigation needed from here */ }
    ProfileScreen(state = state, onAction = viewModel::onAction)
}

@Composable
fun ProfileScreen(
    state   : ProfileState,
    onAction: (ProfileAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar   = {
            AppTopBar(
                title = "Profile",
                actions = {
                    AppButton(
                        text      = if (state.isEditMode) "Cancel" else "Edit",
                        onClick   = { onAction(ProfileAction.OnToggleEditMode) },
                        type      = AppButtonType.Text,
                        fullWidth = false,
                    )
                },
            )
        },
    ) { padding ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        Column(
            modifier            = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Spacer(Modifier.height(24.dp))

            AppImage(
                source             = state.profile?.avatarUrl
                    ?.let { AppImageSource.UrlSource(it) }
                    ?: AppImageSource.Unknown,
                contentDescription = null,
                modifier           = Modifier.size(96.dp).clip(CircleShape),
            )

            Spacer(Modifier.height(16.dp))

            if (!state.isEditMode) {
                Text(
                    text  = state.profile?.fullName ?: "",
                    style = MaterialTheme.typography.headlineSmall,
                )
                state.profile?.username?.takeIf { it.isNotBlank() }?.let {
                    Text(
                        text  = "@$it",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                state.profile?.bio?.takeIf { it.isNotBlank() }?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(text = it, style = MaterialTheme.typography.bodyMedium)
                }
                state.profile?.email?.let {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text  = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            } else {
                AppTextField(
                    value         = state.firstName,
                    onValueChange = { onAction(ProfileAction.OnFirstNameChange(it)) },
                    label         = "First Name",
                    modifier      = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(12.dp))
                AppTextField(
                    value         = state.lastName,
                    onValueChange = { onAction(ProfileAction.OnLastNameChange(it)) },
                    label         = "Last Name",
                    modifier      = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(12.dp))
                AppTextField(
                    value         = state.username,
                    onValueChange = { onAction(ProfileAction.OnUsernameChange(it)) },
                    label         = "Username",
                    placeholder   = "@username",
                    modifier      = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(12.dp))
                AppTextField(
                    value         = state.bio,
                    onValueChange = { onAction(ProfileAction.OnBioChange(it)) },
                    label         = "Bio",
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
                    text      = "Save Changes",
                    onClick   = { onAction(ProfileAction.OnSaveClick) },
                    enabled   = state.isFormValid,
                    isLoading = state.isSaving,
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}
