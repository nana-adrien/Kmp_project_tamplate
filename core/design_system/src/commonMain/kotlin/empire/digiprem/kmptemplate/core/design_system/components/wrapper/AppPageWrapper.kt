package empire.digiprem.kmptemplate.core.design_system.components.wrapper

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import empire.digiprem.kmptemplate.core.design_system.components.dialog.AppErrorDialog
import empire.digiprem.kmptemplate.core.design_system.components.states.AppErrorLevel
import empire.digiprem.kmptemplate.core.design_system.components.states.AppLoadingSpinner
import empire.digiprem.kmptemplate.core.design_system.components.states.AppPageState

@Composable
fun AppPageWrapper(
    state: AppPageState,
    modifier: Modifier = Modifier,
    errorPage: @Composable ((AppErrorLevel.Page) -> Unit)? = null,
    errorDialog: @Composable ((AppErrorLevel.Dialog) -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when {
            state.isLoading -> AppLoadingSpinner()

            state.error is AppErrorLevel.Page && errorPage != null ->
                errorPage(state.error as AppErrorLevel.Page)

            else -> {
                content()
                val dialogError = state.error as? AppErrorLevel.Dialog
                if (dialogError != null) {
                    if (errorDialog != null) {
                        errorDialog(dialogError)
                    } else {
                        AppErrorDialog(
                            message   = dialogError.message,
                            onDismiss = { state.clearError() },
                        )
                    }
                }
            }
        }
    }
}
