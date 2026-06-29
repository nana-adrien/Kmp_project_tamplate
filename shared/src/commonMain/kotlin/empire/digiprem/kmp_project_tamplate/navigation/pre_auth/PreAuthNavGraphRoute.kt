package empire.digiprem.kmp_project_tamplate.navigation.pre_auth

import kotlinx.serialization.Serializable

sealed interface PreAuthNavGraphRoute {
    @Serializable data object PreAuth : PreAuthNavGraphRoute
    @Serializable data object Splash  : PreAuthNavGraphRoute
}
