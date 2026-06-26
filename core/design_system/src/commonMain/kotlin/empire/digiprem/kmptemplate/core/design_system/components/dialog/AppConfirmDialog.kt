package empire.digiprem.kmptemplate.core.design_system.components.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import empire.digiprem.kmptemplate.core.design_system.generated.resources.Res
import empire.digiprem.kmptemplate.core.design_system.generated.resources.btn_cancel
import empire.digiprem.kmptemplate.core.design_system.generated.resources.btn_confirm
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppConfirmDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    confirmText: String = stringResource(Res.string.btn_confirm),
    cancelText: String = stringResource(Res.string.btn_cancel),
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier         = modifier,
        title  = { Text(text = title, style = MaterialTheme.typography.titleMedium) },
        text   = { Text(text = message, style = MaterialTheme.typography.bodyMedium) },
        confirmButton = {
            TextButton(onClick = { onConfirm(); onDismiss() }) {
                Text(text = confirmText, color = MaterialTheme.colorScheme.primary)
            }
        },
        dismissButton = {
            TextButton(onClick = { onCancel(); onDismiss() }) {
                Text(text = cancelText)
            }
        },
    )
}
