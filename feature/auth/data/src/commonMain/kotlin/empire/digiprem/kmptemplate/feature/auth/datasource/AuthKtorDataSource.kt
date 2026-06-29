package empire.digiprem.kmptemplate.feature.auth.datasource

import empire.digiprem.kmptemplate.core.data.dto.ApiResponse
import empire.digiprem.kmptemplate.core.data.dto.ApiResponseWithPayload
import empire.digiprem.kmptemplate.core.data.networking.get
import empire.digiprem.kmptemplate.core.data.networking.post
import empire.digiprem.kmptemplate.core.domain.network.DataError
import empire.digiprem.kmptemplate.core.domain.util.Result
import empire.digiprem.kmptemplate.core.domain.util.map
import empire.digiprem.kmptemplate.feature.auth.dto.CreateProfileRequest
import empire.digiprem.kmptemplate.feature.auth.dto.ForgotPasswordRequest
import empire.digiprem.kmptemplate.feature.auth.dto.LoginRequest
import empire.digiprem.kmptemplate.feature.auth.dto.LoginResponse
import empire.digiprem.kmptemplate.feature.auth.dto.CreateProfileResponse
import empire.digiprem.kmptemplate.feature.auth.dto.ResetPasswordRequest
import empire.digiprem.kmptemplate.feature.auth.dto.VerifyIdentityRequest
import empire.digiprem.kmptemplate.feature.auth.dto.VerifyIdentityResponse
import empire.digiprem.kmptemplate.feature.auth.mapper.toDomain
import empire.digiprem.kmptemplate.feature.auth.model.AuthSession
import empire.digiprem.kmptemplate.feature.auth.model.UserProfile
import io.ktor.client.HttpClient

class AuthKtorDataSource(private val client: HttpClient) : IAuthDataSource {

    override suspend fun login(
        email   : String,
        password: String,
    ): Result<AuthSession?, DataError.Remote> =
        client.post<LoginRequest, ApiResponseWithPayload<LoginResponse>>(
            route = "v1/auth/login",
            body  = LoginRequest(email, password),
        ).map { it.payload?.toDomain() }

    override suspend fun forgotPassword(email: String): Result<Unit?, DataError.Remote> =
        client.post<ForgotPasswordRequest, ApiResponse>(
            route = "v1/auth/forgot-password",
            body  = ForgotPasswordRequest(email),
        ).map { null }

    override suspend fun resetPassword(
        token      : String,
        newPassword: String,
    ): Result<Unit?, DataError.Remote> =
        client.post<ResetPasswordRequest, ApiResponse>(
            route = "v1/auth/reset-password",
            body  = ResetPasswordRequest(token, newPassword),
        ).map { null }

    override suspend fun verifyIdentity(code: String): Result<Boolean?, DataError.Remote> =
        client.post<VerifyIdentityRequest, ApiResponseWithPayload<VerifyIdentityResponse>>(
            route = "v1/auth/verify-identity",
            body  = VerifyIdentityRequest(code),
        ).map { it.payload?.isVerified }

    override suspend fun createProfile(
        firstName: String,
        lastName : String,
        username : String,
    ): Result<UserProfile?, DataError.Remote> =
        client.post<CreateProfileRequest, ApiResponseWithPayload<CreateProfileResponse>>(
            route = "v1/auth/profile",
            body  = CreateProfileRequest(firstName, lastName, username),
        ).map { it.payload?.toDomain() }
}
