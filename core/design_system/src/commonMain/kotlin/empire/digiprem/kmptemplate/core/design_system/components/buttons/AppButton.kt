package empire.digiprem.kmptemplate.core.design_system.components.buttons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class AppButtonType { Primary, Secondary, Text }

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: AppButtonType = AppButtonType.Primary,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    fullWidth: Boolean = true,
) {
    val resolvedModifier = if (fullWidth) modifier.fillMaxWidth().height(52.dp) else modifier.height(52.dp)

    when (type) {
        AppButtonType.Primary -> Button(
            onClick          = onClick,
            modifier         = resolvedModifier,
            enabled          = enabled && !isLoading,
            contentPadding   = PaddingValues(horizontal = 24.dp),
        ) {
            ButtonContent(text = text, isLoading = isLoading)
        }

        AppButtonType.Secondary -> OutlinedButton(
            onClick          = onClick,
            modifier         = resolvedModifier,
            enabled          = enabled && !isLoading,
            contentPadding   = PaddingValues(horizontal = 24.dp),
        ) {
            ButtonContent(text = text, isLoading = isLoading)
        }

        AppButtonType.Text -> TextButton(
            onClick  = onClick,
            modifier = resolvedModifier,
            enabled  = enabled && !isLoading,
        ) {
            ButtonContent(text = text, isLoading = isLoading)
        }
    }
}

@Composable
private fun ButtonContent(text: String, isLoading: Boolean) {
    Box(contentAlignment = Alignment.Center) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier  = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color     = MaterialTheme.colorScheme.onPrimary,
            )
        } else {
            Text(text = text, style = MaterialTheme.typography.labelLarge)
        }
    }
}
