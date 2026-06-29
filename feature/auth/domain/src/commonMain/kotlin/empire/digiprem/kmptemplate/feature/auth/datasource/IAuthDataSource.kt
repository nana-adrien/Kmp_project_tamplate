package empire.digiprem.kmptemplate.feature.auth.datasource

import empire.digiprem.kmptemplate.core.domain.network.DataError
import empire.digiprem.kmptemplate.core.domain.util.Result
import empire.digiprem.kmptemplate.feature.auth.model.AuthSession
import empire.digiprem.kmptemplate.feature.auth.model.UserProfile

interface IAuthDataSource {
    suspend fun login(email: String, password: String): Result<AuthSession?, DataError.Remote>
    suspend fun forgotPassword(email: String): Result<Unit?, DataError.Remote>
    suspend fun resetPassword(token: String, newPassword: String): Result<Unit?, DataError.Remote>
    suspend fun verifyIdentity(code: String): Result<Boolean?, DataError.Remote>
    suspend fun createProfile(
        firstName: String,
        lastName : String,
        username : String,
    ): Result<UserProfile?, DataError.Remote>
}
