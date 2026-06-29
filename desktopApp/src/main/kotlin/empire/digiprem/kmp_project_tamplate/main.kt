package empire.digiprem.kmp_project_tamplate

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import empire.digiprem.kmp_project_tamplate.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title          = "KmpTemplate",
        ) {
            App()
        }
    }
}
