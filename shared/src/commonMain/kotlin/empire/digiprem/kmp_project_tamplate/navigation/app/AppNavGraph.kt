package empire.digiprem.kmp_project_tamplate.navigation.app

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import empire.digiprem.kmptemplate.feature.dashboard.dashboard.DashboardRoot

fun NavGraphBuilder.appNavGraph(
    navController : NavHostController,
    onLogout      : () -> Unit,
) {
    navigation<AppNavGraphRoute.Application>(startDestination = AppNavGraphRoute.Dashboard) {
        composable<AppNavGraphRoute.Dashboard> {
            DashboardRoot(onLogout = onLogout)
        }
    }
}
