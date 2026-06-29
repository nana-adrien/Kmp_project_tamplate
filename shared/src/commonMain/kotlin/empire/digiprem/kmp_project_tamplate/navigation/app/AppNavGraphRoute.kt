package empire.digiprem.kmp_project_tamplate.navigation.app

import kotlinx.serialization.Serializable

sealed interface AppNavGraphRoute {
    @Serializable data object Application : AppNavGraphRoute
    @Serializable data object Dashboard   : AppNavGraphRoute
}
