package empire.digiprem.kmptemplate.feature.profile.datasource

import empire.digiprem.kmptemplate.core.data.dto.ApiResponseWithPayload
import empire.digiprem.kmptemplate.core.data.networking.get
import empire.digiprem.kmptemplate.core.data.networking.put
import empire.digiprem.kmptemplate.core.domain.network.DataError
import empire.digiprem.kmptemplate.core.domain.util.Result
import empire.digiprem.kmptemplate.core.domain.util.map
import empire.digiprem.kmptemplate.feature.profile.datasource.IProfileDataSource
import empire.digiprem.kmptemplate.feature.profile.dto.ProfileDto
import empire.digiprem.kmptemplate.feature.profile.dto.UpdateProfileRequest
import empire.digiprem.kmptemplate.feature.profile.mapper.toDomain
import empire.digiprem.kmptemplate.feature.profile.mapper.toUpdateRequest
import empire.digiprem.kmptemplate.feature.profile.model.Profile
import io.ktor.client.HttpClient

class ProfileKtorDataSource(
    private val client: HttpClient,
) : IProfileDataSource {

    override suspend fun getProfile(): Result<Profile?, DataError.Remote> =
        client.get<ApiResponseWithPayload<ProfileDto>>(route = "v1/profile")
            .map { it.payload?.toDomain() }

    override suspend fun updateProfile(profile: Profile): Result<Profile?, DataError.Remote> =
        client.put<UpdateProfileRequest, ApiResponseWithPayload<ProfileDto>>(
            route = "v1/profile",
            body  = profile.toUpdateRequest(),
        ).map { it.payload?.toDomain() }
}
