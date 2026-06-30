package empire.digiprem.kmptemplate.server.common.util

import empire.digiprem.kmptemplate.server.common.exception.InvalidTokenException
import org.springframework.security.core.context.SecurityContextHolder

/**
 * Returns the userId (as String) of the currently authenticated user from the SecurityContext.
 * Throws [InvalidTokenException] if no authenticated user is present.
 */
fun currentUserId(): String {
    val principal = SecurityContextHolder.getContext().authentication?.principal
    return principal as? String
        ?: throw InvalidTokenException("No authenticated user found in security context")
}
