package empire.digiprem.kmptemplate.feature.notifications.usecase

import empire.digiprem.kmptemplate.core.domain.util.Result
import empire.digiprem.kmptemplate.core.domain.util.mapFailure
import empire.digiprem.kmptemplate.feature.notifications.error.NotificationError
import empire.digiprem.kmptemplate.feature.notifications.repository.INotificationRepository

class MarkAsReadUseCase(private val repository: INotificationRepository) {
    suspend operator fun invoke(id: String): Result<Unit?, NotificationError> =
        repository.markAsRead(id).mapFailure { NotificationError.Unknown }
}
