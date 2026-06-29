package empire.digiprem.kmp_project_tamplate

import androidx.compose.runtime.Composable
import empire.digiprem.kmptemplate.core.design_system.theme.AppTheme
import empire.digiprem.kmp_project_tamplate.navigation.AppNavigation

@Composable
fun App() {
    AppTheme {
        AppNavigation()
    }
}
