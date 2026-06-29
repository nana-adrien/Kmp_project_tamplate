package empire.digiprem.kmptemplate.feature.profile.repository

import empire.digiprem.kmptemplate.core.domain.network.DataError
import empire.digiprem.kmptemplate.core.domain.util.Result
import empire.digiprem.kmptemplate.feature.profile.model.Profile

interface IProfileRepository {
    suspend fun getProfile(): Result<Profile?, DataError.Remote>
    suspend fun updateProfile(profile: Profile): Result<Profile?, DataError.Remote>
}
