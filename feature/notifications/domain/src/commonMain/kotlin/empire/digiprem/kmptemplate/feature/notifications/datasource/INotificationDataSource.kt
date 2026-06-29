package empire.digiprem.kmptemplate.feature.notifications.datasource

import empire.digiprem.kmptemplate.core.domain.network.DataError
import empire.digiprem.kmptemplate.core.domain.util.Result
import empire.digiprem.kmptemplate.feature.notifications.model.Notification

interface INotificationDataSource {
    suspend fun getNotifications(): Result<List<Notification>?, DataError.Remote>
    suspend fun markAsRead(id: String): Result<Unit?, DataError.Remote>
    suspend fun deleteNotification(id: String): Result<Unit?, DataError.Remote>
}
