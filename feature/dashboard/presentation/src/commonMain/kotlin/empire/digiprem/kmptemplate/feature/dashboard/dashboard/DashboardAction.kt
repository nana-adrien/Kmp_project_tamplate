package empire.digiprem.kmptemplate.feature.dashboard.dashboard

sealed interface DashboardAction {
    data class OnDestinationChange(val destination: DashboardDestination) : DashboardAction
}
