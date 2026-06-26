package empire.digiprem.kmptemplate.core.design_system.components.states

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AppLoadingSpinner(
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    centered: Boolean = true,
) {
    if (centered) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                modifier    = Modifier.size(size),
                color       = MaterialTheme.colorScheme.primary,
                strokeWidth = 3.dp,
            )
        }
    } else {
        CircularProgressIndicator(
            modifier    = modifier.size(size),
            color       = MaterialTheme.colorScheme.primary,
            strokeWidth = 3.dp,
        )
    }
}
