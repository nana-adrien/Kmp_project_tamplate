package empire.digiprem.kmptemplate.server.common.exception

class UserAlreadyExistsException : RuntimeException("A user with this email or username already exists")
class UserNotFoundException : RuntimeException("User not found")
class InvalidCredentialsException : RuntimeException("Invalid email or password")
class InvalidTokenException(message: String) : RuntimeException(message)
class EmailNotVerifiedException : RuntimeException("Email address has not been verified")
