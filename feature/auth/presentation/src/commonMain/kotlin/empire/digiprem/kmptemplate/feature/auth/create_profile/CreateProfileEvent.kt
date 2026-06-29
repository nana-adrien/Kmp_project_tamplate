package empire.digiprem.kmptemplate.feature.auth.create_profile

sealed interface CreateProfileEvent {
    data object OnProfileCreated : CreateProfileEvent
}
