package empire.digiprem.kmptemplate.core.design_system.components.topbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import empire.digiprem.kmptemplate.core.design_system.components.buttons.AppIconButton
import empire.digiprem.kmptemplate.core.design_system.components.icon.AppIconResource
import empire.digiprem.kmptemplate.core.design_system.generated.resources.Res
import empire.digiprem.kmptemplate.core.design_system.generated.resources.cd_back
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    modifier: Modifier = Modifier,
    onNavigateBack: (() -> Unit)? = null,
    actions: @Composable (() -> Unit) = {},
) {
    TopAppBar(
        modifier = modifier,
        title    = { Text(text = title, style = MaterialTheme.typography.titleLarge) },
        navigationIcon = {
            if (onNavigateBack != null) {
                AppIconButton(
                    icon               = AppIconResource.VectorResource(Icons.AutoMirrored.Filled.ArrowBack),
                    contentDescription = stringResource(Res.string.cd_back),
                    onClick            = onNavigateBack,
                )
            }
        },
        actions  = { actions() },
        colors   = TopAppBarDefaults.topAppBarColors(
            containerColor       = MaterialTheme.colorScheme.surface,
            titleContentColor    = MaterialTheme.colorScheme.onSurface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface,
        ),
    )
}
