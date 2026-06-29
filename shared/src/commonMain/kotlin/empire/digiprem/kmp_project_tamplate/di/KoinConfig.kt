package empire.digiprem.kmp_project_tamplate.di

import empire.digiprem.kmptemplate.core.config.coreModuleData
import empire.digiprem.kmptemplate.feature.auth.config.koinAuthModule
import empire.digiprem.kmptemplate.feature.dashboard.config.koinDashboardModule
import empire.digiprem.kmptemplate.feature.notifications.config.koinNotificationsModule
import empire.digiprem.kmptemplate.feature.profile.config.koinProfileModule
import empire.digiprem.kmptemplate.feature.settings.config.koinSettingsModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            coreModuleData,
            koinAuthModule,
            koinProfileModule,
            koinNotificationsModule,
            koinSettingsModule,
            koinDashboardModule,
        )
    }
}
