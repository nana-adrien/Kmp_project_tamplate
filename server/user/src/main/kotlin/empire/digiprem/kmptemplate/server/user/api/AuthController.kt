package empire.digiprem.kmptemplate.server.user.api

import empire.digiprem.kmptemplate.contracts.auth.AuthResponse
import empire.digiprem.kmptemplate.contracts.auth.ForgotPasswordRequest
import empire.digiprem.kmptemplate.contracts.auth.LoginRequest
import empire.digiprem.kmptemplate.contracts.auth.RefreshTokenRequest
import empire.digiprem.kmptemplate.contracts.auth.RegisterRequest
import empire.digiprem.kmptemplate.contracts.auth.ResetPasswordRequest
import empire.digiprem.kmptemplate.server.user.service.AuthService
import empire.digiprem.kmptemplate.server.user.service.EmailVerificationService
import empire.digiprem.kmptemplate.server.user.service.PasswordResetService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

//@Tag(name = "accounts")
@RestController
@RequestMapping("/api/v1/auth", name = "Auth")
class AuthController(
    private val authService: AuthService,
    private val emailVerificationService: EmailVerificationService,
    private val passwordResetService: PasswordResetService,
) {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestBody body: RegisterRequest) {
        authService.register(body.email, body.username, body.password)
    }

    @PostMapping("/login")
    fun login(@RequestBody body: LoginRequest): AuthResponse {
        return authService.login(body.email, body.password)
    }

    @PostMapping("/refresh")
    fun refresh(@RequestBody body: RefreshTokenRequest): AuthResponse {
        return authService.refresh(body.refreshToken)
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun logout(@RequestBody body: RefreshTokenRequest) {
        authService.logout(body.refreshToken)
    }

    @GetMapping("/verify")
    fun verifyEmail(@RequestParam token: String) {
        emailVerificationService.verifyEmail(token)
    }

    @PostMapping("/forgot-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun forgotPassword(@RequestBody body: ForgotPasswordRequest) {
        passwordResetService.requestReset(body.email)
    }

    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun resetPassword(@RequestBody body: ResetPasswordRequest) {
        passwordResetService.resetPassword(body.token, body.newPassword)
    }
}
