package empire.digiprem.kmptemplate.core.design_system.components.states

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import empire.digiprem.kmptemplate.core.design_system.components.buttons.AppButton
import empire.digiprem.kmptemplate.core.design_system.generated.resources.Res
import empire.digiprem.kmptemplate.core.design_system.generated.resources.btn_retry
import empire.digiprem.kmptemplate.core.design_system.generated.resources.label_empty
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppStateContent(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    actionText: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Column(
        modifier            = modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector        = icon,
            contentDescription = null,
            modifier           = Modifier.size(72.dp),
            tint               = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text      = title,
            style     = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color     = MaterialTheme.colorScheme.onSurface,
        )
        if (description != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text      = description,
                style     = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color     = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        if (onAction != null) {
            Spacer(modifier = Modifier.height(24.dp))
            AppButton(
                text       = actionText ?: stringResource(Res.string.btn_retry),
                onClick    = onAction,
                fullWidth  = false,
            )
        }
    }
}

@Composable
fun AppEmptyContent(
    modifier: Modifier = Modifier,
    title: String = stringResource(Res.string.label_empty),
    description: String? = null,
    icon: ImageVector? = null,
) {
    if (icon != null) {
        AppStateContent(
            icon        = icon,
            title       = title,
            description = description,
            modifier    = modifier,
        )
    } else {
        Column(
            modifier            = modifier.fillMaxSize().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text      = title,
                style     = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color     = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (description != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text      = description,
                    style     = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color     = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
