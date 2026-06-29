package empire.digiprem.kmptemplate.feature.notifications.datasource

import empire.digiprem.kmptemplate.core.data.dto.ApiResponseWithPayload
import empire.digiprem.kmptemplate.core.data.networking.delete
import empire.digiprem.kmptemplate.core.data.networking.get
import empire.digiprem.kmptemplate.core.data.networking.put
import empire.digiprem.kmptemplate.core.domain.network.DataError
import empire.digiprem.kmptemplate.core.domain.util.Result
import empire.digiprem.kmptemplate.core.domain.util.map
import empire.digiprem.kmptemplate.feature.notifications.dto.NotificationDto
import empire.digiprem.kmptemplate.feature.notifications.mapper.toDomain
import empire.digiprem.kmptemplate.feature.notifications.model.Notification
import io.ktor.client.HttpClient

class NotificationKtorDataSource(
    private val client: HttpClient,
) : INotificationDataSource {

    override suspend fun getNotifications(): Result<List<Notification>?, DataError.Remote> =
        client.get<ApiResponseWithPayload<List<NotificationDto>>>(route = "v1/notifications")
            .map { it.payload?.map { dto -> dto.toDomain() } }

    override suspend fun markAsRead(id: String): Result<Unit?, DataError.Remote> =
        client.put<Unit, ApiResponseWithPayload<Unit>>(
            route = "v1/notifications/$id/read",
            body  = Unit,
        ).map { it.payload }

    override suspend fun deleteNotification(id: String): Result<Unit?, DataError.Remote> =
        client.delete<ApiResponseWithPayload<Unit>>(route = "v1/notifications/$id")
            .map { it.payload }
}
