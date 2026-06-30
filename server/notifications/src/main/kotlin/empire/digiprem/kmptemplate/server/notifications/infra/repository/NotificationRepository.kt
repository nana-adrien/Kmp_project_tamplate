package empire.digiprem.kmptemplate.server.notifications.infra.repository

import empire.digiprem.kmptemplate.server.notifications.infra.entity.NotificationEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface NotificationRepository : JpaRepository<NotificationEntity, UUID> {
    fun findByUserIdOrderByCreatedAtDesc(userId: UUID): List<NotificationEntity>
    fun findByUserIdAndIdIn(userId: UUID, ids: List<UUID>): List<NotificationEntity>
}
