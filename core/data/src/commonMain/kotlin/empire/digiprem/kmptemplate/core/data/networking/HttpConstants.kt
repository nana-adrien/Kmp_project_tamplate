package empire.digiprem.kmptemplate.core.data.networking

object HttpConstants {
    // Replace with your API base URL after setup
    const val BASE_URL = "https://api.yourapp.com/"

    const val ALLOW_ANONYMOUS       = "Allow-Anonymous"
    const val ALLOW_ANONYMOUS_VALUE = "1"
    const val SUBSCRIPTION_KEY      = "Subscription-Key"
    const val TENANT_ID             = "Tenant-Id"
    const val ACCEPT_LANGUAGE       = "Accept-Language"
    const val X_API_KEY             = "X-Api-Key"
    const val X_CORRELATION_ID      = "X-Correlation-Id"

    // Add anonymous endpoints (no auth header)
    private val anonymousEndpoints = setOf(
        "/authenticate",
        "/forgot-password",
        "/reset-password",
        "/register",
    )

    fun isAnonymous(path: String): Boolean =
        anonymousEndpoints.any { path.contains(it) }
}
