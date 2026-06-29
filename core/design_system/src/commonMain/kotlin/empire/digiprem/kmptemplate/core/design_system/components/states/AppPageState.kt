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
    private val _isLoading = mutableStateOf(false)
    private val _error = mutableStateOf<AppErrorLevel?>(null)

    val isLoading: Boolean get() = _isLoading.value
    val error: AppErrorLevel? get() = _error.value

    fun setLoading(loading: Boolean) { _isLoading.value = loading }
    fun setError(level: AppErrorLevel) { _error.value = level; _isLoading.value = false }
    fun clearError() { _error.value = null }
    fun setReady() { _isLoading.value = false; _error.value = null }
}

@Composable
fun rememberAppPageState(): AppPageState = remember { AppPageState() }
