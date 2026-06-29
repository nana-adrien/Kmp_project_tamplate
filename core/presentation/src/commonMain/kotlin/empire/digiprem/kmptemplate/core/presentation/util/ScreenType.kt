package empire.digiprem.kmptemplate.core.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalWindowInfo

enum class ScreenType { Mobile, Tablet, Desktop }

@Composable
fun rememberScreenType(): ScreenType {
    val width = LocalWindowInfo.current.containerSize.width
    return remember(width) {
        when {
            width < 600  -> ScreenType.Mobile
            width < 1024 -> ScreenType.Tablet
            else         -> ScreenType.Desktop
        }
    }
}
