package empire.digiprem.kmptemplate.contracts.validation

import empire.digiprem.kmptemplate.contracts.auth.ForgotPasswordRequest
import empire.digiprem.kmptemplate.contracts.auth.LoginRequest
import empire.digiprem.kmptemplate.contracts.auth.RefreshTokenRequest
import empire.digiprem.kmptemplate.contracts.auth.RegisterRequest
import empire.digiprem.kmptemplate.contracts.auth.ResetPasswordRequest
import io.konform.validation.Validation
import io.konform.validation.constraints.maxLength
import io.konform.validation.constraints.minLength
import io.konform.validation.constraints.notBlank
import io.konform.validation.constraints.pattern

private const val EMAIL_PATTERN = ".+@.+\\..+"

val validateLoginRequest = Validation<LoginRequest> {
    LoginRequest::email {
        notBlank()
        pattern(EMAIL_PATTERN)
    }
    LoginRequest::password {
        notBlank()
    }
}

val validateRegisterRequest = Validation<RegisterRequest> {
    RegisterRequest::email {
        notBlank()
        pattern(EMAIL_PATTERN)
    }
    RegisterRequest::username {
        notBlank()
        minLength(3)
        maxLength(30)
    }
    RegisterRequest::password {
        notBlank()
        minLength(8)
    }
}

val validateForgotPasswordRequest = Validation<ForgotPasswordRequest> {
    ForgotPasswordRequest::email {
        notBlank()
        pattern(EMAIL_PATTERN)
    }
}

val validateResetPasswordRequest = Validation<ResetPasswordRequest> {
    ResetPasswordRequest::token {
        notBlank()
    }
    ResetPasswordRequest::newPassword {
        notBlank()
        minLength(8)
    }
}

val validateRefreshTokenRequest = Validation<RefreshTokenRequest> {
    RefreshTokenRequest::refreshToken {
        notBlank()
    }
}
