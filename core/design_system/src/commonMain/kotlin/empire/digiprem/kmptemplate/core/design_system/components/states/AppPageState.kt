package empire.digiprem.kmptemplate.core.design_system.components.states

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

sealed class AppErrorLevel {
    data class Page(val message: String) : AppErrorLevel()
    data class Dialog(val message: String) : AppErrorLevel()
}

@Stable
class AppPageState {
    var isLoading by mutableStateOf(false)
        private set
    var error: AppErrorLevel? by mutableStateOf(null)
        private set

    fun setLoading(loading: Boolean) { isLoading = loading }
    fun setError(level: AppErrorLevel) { error = level; isLoading = false }
    fun clearError() { error = null }
    fun setReady() { isLoading = false; error = null }
}

@Composable
fun rememberAppPageState(): AppPageState = remember { AppPageState() }
