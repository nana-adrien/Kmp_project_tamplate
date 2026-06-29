package empire.digiprem.kmptemplate.feature.profile.profile

sealed interface ProfileEvent {
    data object OnProfileUpdated : ProfileEvent
}
