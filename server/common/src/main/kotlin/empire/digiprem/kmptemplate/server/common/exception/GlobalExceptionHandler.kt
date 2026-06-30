package empire.digiprem.kmptemplate.server.common.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleBadCredentials(e: BadCredentialsException) = errorBody("INVALID_CREDENTIALS", e.message)

    @ExceptionHandler(UsernameNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleUsernameNotFound(e: UsernameNotFoundException) = errorBody("USER_NOT_FOUND", e.message)

    @ExceptionHandler(UserAlreadyExistsException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleUserAlreadyExists(e: UserAlreadyExistsException) = errorBody("USER_EXISTS", e.message)

    @ExceptionHandler(UserNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleUserNotFound(e: UserNotFoundException) = errorBody("USER_NOT_FOUND", e.message)

    @ExceptionHandler(InvalidCredentialsException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleInvalidCredentials(e: InvalidCredentialsException) = errorBody("INVALID_CREDENTIALS", e.message)

    @ExceptionHandler(InvalidTokenException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleInvalidToken(e: InvalidTokenException) = errorBody("INVALID_TOKEN", e.message)

    @ExceptionHandler(EmailNotVerifiedException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleEmailNotVerified(e: EmailNotVerifiedException) = errorBody("EMAIL_NOT_VERIFIED", e.message)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(e: MethodArgumentNotValidException): ResponseEntity<Map<String, Any>> {
        val errors = e.bindingResult.allErrors.map { it.defaultMessage ?: "Invalid value" }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            mapOf("code" to "VALIDATION_ERROR", "errors" to errors)
        )
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleGeneric(e: Exception) = errorBody("INTERNAL_ERROR", e.message ?: "An unexpected error occurred")

    private fun errorBody(code: String, message: String?) =
        mapOf("code" to code, "message" to (message ?: "Unknown error"))
}
