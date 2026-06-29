package empire.digiprem.kmptemplate.feature.notifications.usecase

import empire.digiprem.kmptemplate.core.domain.util.Result
import empire.digiprem.kmptemplate.core.domain.util.mapFailure
import empire.digiprem.kmptemplate.feature.notifications.error.NotificationError
import empire.digiprem.kmptemplate.feature.notifications.model.Notification
import empire.digiprem.kmptemplate.feature.notifications.repository.INotificationRepository

class GetNotificationsUseCase(private val repository: INotificationRepository) {
    suspend operator fun invoke(): Result<List<Notification>?, NotificationError> =
        repository.getNotifications().mapFailure { NotificationError.Unknown }
}
