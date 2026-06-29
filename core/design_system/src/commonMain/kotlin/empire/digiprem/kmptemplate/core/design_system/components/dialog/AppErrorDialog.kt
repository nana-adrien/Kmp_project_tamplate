package empire.digiprem.kmptemplate.core.design_system.components.dialog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import empire.digiprem.kmptemplate.core.design_system.generated.resources.Res
import empire.digiprem.kmptemplate.core.design_system.generated.resources.btn_ok
import empire.digiprem.kmptemplate.core.design_system.generated.resources.label_error
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppErrorDialog(
    message: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = stringResource(Res.string.label_error),
    dismissText: String = stringResource(Res.string.btn_ok),
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier         = modifier,
        icon   = { Icon(imageVector = Icons.Default.Error, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
        title  = { Text(text = title, style = MaterialTheme.typography.titleMedium) },
        text   = { Text(text = message, style = MaterialTheme.typography.bodyMedium) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = dismissText)
            }
        },
    )
}
