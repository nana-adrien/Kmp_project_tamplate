package empire.digiprem.kmptemplate.feature.profile.config

import empire.digiprem.kmptemplate.feature.profile.datasource.IProfileDataSource
import empire.digiprem.kmptemplate.feature.profile.datasource.ProfileKtorDataSource
import empire.digiprem.kmptemplate.feature.profile.profile.ProfileViewModel
import empire.digiprem.kmptemplate.feature.profile.repository.IProfileRepository
import empire.digiprem.kmptemplate.feature.profile.repository.ProfileRepository
import empire.digiprem.kmptemplate.feature.profile.usecase.GetProfileUseCase
import empire.digiprem.kmptemplate.feature.profile.usecase.UpdateProfileUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val koinProfileModule = module {
    singleOf(::ProfileKtorDataSource) bind IProfileDataSource::class
    singleOf(::ProfileRepository)     bind IProfileRepository::class
    singleOf(::GetProfileUseCase)
    singleOf(::UpdateProfileUseCase)
    viewModelOf(::ProfileViewModel)
}
