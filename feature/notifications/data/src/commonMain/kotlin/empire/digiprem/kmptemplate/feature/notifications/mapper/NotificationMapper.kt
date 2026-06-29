package empire.digiprem.kmptemplate.feature.notifications.mapper

import empire.digiprem.kmptemplate.feature.notifications.dto.NotificationDto
import empire.digiprem.kmptemplate.feature.notifications.model.Notification

fun NotificationDto.toDomain(): Notification = Notification(
    id        = id,
    title     = title,
    body      = body,
    isRead    = isRead,
    createdAt = createdAt,
    type      = type,
)
