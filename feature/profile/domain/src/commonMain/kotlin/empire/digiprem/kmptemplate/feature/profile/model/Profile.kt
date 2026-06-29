package empire.digiprem.kmptemplate.feature.profile.model

data class Profile(
    val id        : String,
    val email     : String,
    val firstName : String,
    val lastName  : String,
    val username  : String  = "",
    val avatarUrl : String? = null,
    val bio       : String? = null,
) {
    val fullName: String get() = "$firstName $lastName".trim()
}
