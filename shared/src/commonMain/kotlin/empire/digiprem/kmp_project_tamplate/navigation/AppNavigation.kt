package empire.digiprem.kmp_project_tamplate.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import empire.digiprem.kmp_project_tamplate.navigation.app.AppNavGraphRoute
import empire.digiprem.kmp_project_tamplate.navigation.app.appNavGraph
import empire.digiprem.kmp_project_tamplate.navigation.auth.AuthNavGraphRoute
import empire.digiprem.kmp_project_tamplate.navigation.auth.authNavGraph
import empire.digiprem.kmp_project_tamplate.navigation.pre_auth.PreAuthNavGraphRoute
import empire.digiprem.kmp_project_tamplate.navigation.pre_auth.preAuthNavGraph

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController    = navController,
        startDestination = PreAuthNavGraphRoute.PreAuth,
    ) {
        preAuthNavGraph(
            navController    = navController,
            onNavigateToAuth = {
                navController.navigate(AuthNavGraphRoute.Auth) {
                    popUpTo(PreAuthNavGraphRoute.PreAuth) { inclusive = true }
                }
            },
        )

        authNavGraph(
            navController = navController,
            onAuthSuccess = {
                navController.navigate(AppNavGraphRoute.Application) {
                    popUpTo(AuthNavGraphRoute.Auth) { inclusive = true }
                }
            },
        )

        appNavGraph(
            navController = navController,
            onLogout      = {
                navController.navigate(AuthNavGraphRoute.Auth) {
                    popUpTo(AppNavGraphRoute.Application) { inclusive = true }
                }
            },
        )
    }
}
