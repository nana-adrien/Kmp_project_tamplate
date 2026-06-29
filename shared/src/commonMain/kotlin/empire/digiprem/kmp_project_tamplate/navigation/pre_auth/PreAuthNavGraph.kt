package empire.digiprem.kmp_project_tamplate.navigation.pre_auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation

fun NavGraphBuilder.preAuthNavGraph(
    navController    : NavHostController,
    onNavigateToAuth : () -> Unit,
) {
    navigation<PreAuthNavGraphRoute.PreAuth>(startDestination = PreAuthNavGraphRoute.Splash) {
        composable<PreAuthNavGraphRoute.Splash> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            LaunchedEffect(Unit) {
                onNavigateToAuth()
            }
        }
    }
}
