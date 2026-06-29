package empire.digiprem.kmp_project_tamplate.navigation.auth

import kotlinx.serialization.Serializable

sealed interface AuthNavGraphRoute {
    @Serializable data object Auth           : AuthNavGraphRoute
    @Serializable data object Login          : AuthNavGraphRoute
    @Serializable data object ForgotPassword : AuthNavGraphRoute
    @Serializable data object ResetPassword  : AuthNavGraphRoute
    @Serializable data object VerifyIdentity : AuthNavGraphRoute
    @Serializable data object CreateProfile  : AuthNavGraphRoute
}
