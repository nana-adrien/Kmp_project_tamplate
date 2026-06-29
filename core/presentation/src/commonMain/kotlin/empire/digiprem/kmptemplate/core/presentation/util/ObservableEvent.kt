package empire.digiprem.kmptemplate.core.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow

@Composable
fun <T> ObservableEvent(
    flow: Flow<T>,
    onEvent: suspend (T) -> Unit,
) {
    LaunchedEffect(Unit) {
        flow.collect(onEvent)
    }
}
