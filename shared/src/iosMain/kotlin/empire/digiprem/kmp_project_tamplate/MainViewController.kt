package empire.digiprem.kmp_project_tamplate

import androidx.compose.ui.window.ComposeUIViewController
import empire.digiprem.kmp_project_tamplate.di.initKoin
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    initKoin()
    return ComposeUIViewController { App() }
}
