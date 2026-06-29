package empire.digiprem.kmptemplate.feature.dashboard.config

import empire.digiprem.kmptemplate.feature.dashboard.dashboard.DashboardViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val koinDashboardModule = module {
    viewModelOf(::DashboardViewModel)
}
