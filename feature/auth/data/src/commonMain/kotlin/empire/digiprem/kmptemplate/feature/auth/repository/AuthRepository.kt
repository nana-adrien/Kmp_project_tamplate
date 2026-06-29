package empire.digiprem.kmptemplate.feature.auth.repository

import empire.digiprem.kmptemplate.core.domain.network.DataError
import empire.digiprem.kmptemplate.core.domain.util.Result
import empire.digiprem.kmptemplate.feature.auth.datasource.IAuthDataSource
import empire.digiprem.kmptemplate.feature.auth.model.AuthSession
import empire.digiprem.kmptemplate.feature.auth.model.UserProfile

class AuthRepository(
    private val dataSource: IAuthDataSource,
) : IAuthRepository {

    override suspend fun login(email: String, password: String): Result<AuthSession?, DataError.Remote> =
        dataSource.login(email, password)

    override suspend fun forgotPassword(email: String): Result<Unit?, DataError.Remote> =
        dataSource.forgotPassword(email)

    override suspend fun resetPassword(token: String, newPassword: String): Result<Unit?, DataError.Remote> =
        dataSource.resetPassword(token, newPassword)

    override suspend fun verifyIdentity(code: String): Result<Boolean?, DataError.Remote> =
        dataSource.verifyIdentity(code)

    override suspend fun createProfile(
        firstName: String,
        lastName : String,
        username : String,
    ): Result<UserProfile?, DataError.Remote> =
        dataSource.createProfile(firstName, lastName, username)
}
