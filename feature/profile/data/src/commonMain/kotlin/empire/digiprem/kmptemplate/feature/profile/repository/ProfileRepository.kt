package empire.digiprem.kmptemplate.feature.profile.repository

import empire.digiprem.kmptemplate.core.domain.network.DataError
import empire.digiprem.kmptemplate.core.domain.util.Result
import empire.digiprem.kmptemplate.feature.profile.datasource.IProfileDataSource
import empire.digiprem.kmptemplate.feature.profile.model.Profile

class ProfileRepository(
    private val dataSource: IProfileDataSource,
) : IProfileRepository {

    override suspend fun getProfile(): Result<Profile?, DataError.Remote> =
        dataSource.getProfile()

    override suspend fun updateProfile(profile: Profile): Result<Profile?, DataError.Remote> =
        dataSource.updateProfile(profile)
}
