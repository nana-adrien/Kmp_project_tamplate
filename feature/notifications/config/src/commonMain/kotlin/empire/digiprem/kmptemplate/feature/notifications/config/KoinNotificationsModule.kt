package empire.digiprem.kmptemplate.feature.notifications.config

import empire.digiprem.kmptemplate.feature.notifications.datasource.INotificationDataSource
import empire.digiprem.kmptemplate.feature.notifications.datasource.NotificationKtorDataSource
import empire.digiprem.kmptemplate.feature.notifications.notifications.NotificationsViewModel
import empire.digiprem.kmptemplate.feature.notifications.repository.INotificationRepository
import empire.digiprem.kmptemplate.feature.notifications.repository.NotificationRepository
import empire.digiprem.kmptemplate.feature.notifications.usecase.DeleteNotificationUseCase
import empire.digiprem.kmptemplate.feature.notifications.usecase.GetNotificationsUseCase
import empire.digiprem.kmptemplate.feature.notifications.usecase.MarkAsReadUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val koinNotificationsModule = module {
    singleOf(::NotificationKtorDataSource) bind INotificationDataSource::class
    singleOf(::NotificationRepository)     bind INotificationRepository::class
    singleOf(::GetNotificationsUseCase)
    singleOf(::MarkAsReadUseCase)
    singleOf(::DeleteNotificationUseCase)
    viewModelOf(::NotificationsViewModel)
}
