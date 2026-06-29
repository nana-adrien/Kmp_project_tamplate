package empire.digiprem.kmptemplate.feature.auth.model

data class UserProfile(
    val id        : String,
    val email     : String,
    val firstName : String,
    val lastName  : String,
    val username  : String  = "",
    val avatarUrl : String? = null,
) {
    val fullName: String get() = "$firstName $lastName".trim()
}
