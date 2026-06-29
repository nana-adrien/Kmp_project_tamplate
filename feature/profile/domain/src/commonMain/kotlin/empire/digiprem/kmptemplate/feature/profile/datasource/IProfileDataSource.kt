package empire.digiprem.kmptemplate.feature.profile.datasource

import empire.digiprem.kmptemplate.core.domain.network.DataError
import empire.digiprem.kmptemplate.core.domain.util.Result
import empire.digiprem.kmptemplate.feature.profile.model.Profile

interface IProfileDataSource {
    suspend fun getProfile(): Result<Profile?, DataError.Remote>
    suspend fun updateProfile(profile: Profile): Result<Profile?, DataError.Remote>
}
