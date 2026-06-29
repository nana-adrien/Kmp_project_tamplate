package empire.digiprem.kmptemplate.feature.notifications.repository

import empire.digiprem.kmptemplate.core.domain.network.DataError
import empire.digiprem.kmptemplate.core.domain.util.Result
import empire.digiprem.kmptemplate.feature.notifications.datasource.INotificationDataSource
import empire.digiprem.kmptemplate.feature.notifications.model.Notification

class NotificationRepository(
    private val dataSource: INotificationDataSource,
) : INotificationRepository {

    override suspend fun getNotifications(): Result<List<Notification>?, DataError.Remote> =
        dataSource.getNotifications()

    override suspend fun markAsRead(id: String): Result<Unit?, DataError.Remote> =
        dataSource.markAsRead(id)

    override suspend fun deleteNotification(id: String): Result<Unit?, DataError.Remote> =
        dataSource.deleteNotification(id)
}
