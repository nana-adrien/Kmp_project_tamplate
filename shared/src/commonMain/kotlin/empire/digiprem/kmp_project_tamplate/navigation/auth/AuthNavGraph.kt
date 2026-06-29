package empire.digiprem.kmp_project_tamplate.navigation.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import empire.digiprem.kmptemplate.feature.auth.create_profile.CreateProfileRoot
import empire.digiprem.kmptemplate.feature.auth.forgot_password.ForgotPasswordRoot
import empire.digiprem.kmptemplate.feature.auth.login.LoginRoot
import empire.digiprem.kmptemplate.feature.auth.reset_password.ResetPasswordRoot
import empire.digiprem.kmptemplate.feature.auth.verify_identity.VerifyIdentityRoot

fun NavGraphBuilder.authNavGraph(
    navController : NavHostController,
    onAuthSuccess : () -> Unit,
) {
    navigation<AuthNavGraphRoute.Auth>(startDestination = AuthNavGraphRoute.Login) {
        composable<AuthNavGraphRoute.Login> {
            LoginRoot(
                onLoginSuccess   = onAuthSuccess,
                onForgotPassword = { navController.navigate(AuthNavGraphRoute.ForgotPassword) },
            )
        }
        composable<AuthNavGraphRoute.ForgotPassword> {
            ForgotPasswordRoot(
                onSuccess      = { navController.navigate(AuthNavGraphRoute.ResetPassword) },
                onNavigateBack = { navController.popBackStack() },
            )
        }
        composable<AuthNavGraphRoute.ResetPassword> {
            ResetPasswordRoot(
                onSuccess      = { navController.navigate(AuthNavGraphRoute.VerifyIdentity) },
                onNavigateBack = { navController.popBackStack() },
            )
        }
        composable<AuthNavGraphRoute.VerifyIdentity> {
            VerifyIdentityRoot(
                onVerified     = { navController.navigate(AuthNavGraphRoute.CreateProfile) },
                onNavigateBack = { navController.popBackStack() },
            )
        }
        composable<AuthNavGraphRoute.CreateProfile> {
            CreateProfileRoot(
                onProfileCreated = onAuthSuccess,
            )
        }
    }
}
