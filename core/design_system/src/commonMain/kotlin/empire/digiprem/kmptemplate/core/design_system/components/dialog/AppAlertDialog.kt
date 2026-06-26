package empire.digiprem.kmptemplate.core.design_system.components.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AppAlertDialog(
    title: String,
    message: String,
    confirmText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    dismissText: String? = null,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier         = modifier,
        title            = { Text(text = title, style = MaterialTheme.typography.titleMedium) },
        text             = { Text(text = message, style = MaterialTheme.typography.bodyMedium) },
        confirmButton    = {
            TextButton(onClick = onConfirm) {
                Text(text = confirmText)
            }
        },
        dismissButton    = dismissText?.let {
            { TextButton(onClick = onDismiss) { Text(text = it) } }
        },
    )
}
