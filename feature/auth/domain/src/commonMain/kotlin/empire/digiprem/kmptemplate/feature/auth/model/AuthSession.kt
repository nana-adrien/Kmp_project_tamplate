package empire.digiprem.kmptemplate.feature.auth.model

data class AuthSession(
    val accessToken  : String,
    val refreshToken : String,
    val expiresAt    : Long,
    val userId       : String,
    val isVerified   : Boolean = false,
) {
    val isExpired: Boolean
        get() = System.currentTimeMillis() / 1000 >= expiresAt
}
