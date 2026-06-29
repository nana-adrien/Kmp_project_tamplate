package empire.digiprem.kmptemplate.feature.dashboard.dashboard

sealed interface DashboardEvent {
    data object OnLogout : DashboardEvent
}
