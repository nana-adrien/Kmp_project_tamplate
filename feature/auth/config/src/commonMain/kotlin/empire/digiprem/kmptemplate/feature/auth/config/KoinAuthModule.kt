package empire.digiprem.kmptemplate.feature.auth.config

import empire.digiprem.kmptemplate.feature.auth.create_profile.CreateProfileViewModel
import empire.digiprem.kmptemplate.feature.auth.datasource.AuthKtorDataSource
import empire.digiprem.kmptemplate.feature.auth.datasource.IAuthDataSource
import empire.digiprem.kmptemplate.feature.auth.forgot_password.ForgotPasswordViewModel
import empire.digiprem.kmptemplate.feature.auth.login.LoginViewModel
import empire.digiprem.kmptemplate.feature.auth.repository.AuthRepository
import empire.digiprem.kmptemplate.feature.auth.repository.IAuthRepository
import empire.digiprem.kmptemplate.feature.auth.reset_password.ResetPasswordViewModel
import empire.digiprem.kmptemplate.feature.auth.usecase.CreateProfileUseCase
import empire.digiprem.kmptemplate.feature.auth.usecase.ForgotPasswordUseCase
import empire.digiprem.kmptemplate.feature.auth.usecase.LoginUseCase
import empire.digiprem.kmptemplate.feature.auth.usecase.ResetPasswordUseCase
import empire.digiprem.kmptemplate.feature.auth.usecase.VerifyIdentityUseCase
import empire.digiprem.kmptemplate.feature.auth.verify_identity.VerifyIdentityViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val koinAuthModule = module {
    // Data layer
    singleOf(::AuthKtorDataSource) bind IAuthDataSource::class
    singleOf(::AuthRepository)     bind IAuthRepository::class

    // Domain — use cases
    singleOf(::LoginUseCase)
    singleOf(::ForgotPasswordUseCase)
    singleOf(::ResetPasswordUseCase)
    singleOf(::VerifyIdentityUseCase)
    singleOf(::CreateProfileUseCase)

    // Presentation — view models
    viewModelOf(::LoginViewModel)
    viewModelOf(::ForgotPasswordViewModel)
    viewModelOf(::ResetPasswordViewModel)
    viewModelOf(::VerifyIdentityViewModel)
    viewModelOf(::CreateProfileViewModel)
}
