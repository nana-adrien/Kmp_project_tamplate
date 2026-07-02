package empire.digiprem.kmptemplate.server.notifications.infra.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "notifications")
class NotificationEntity(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @Column(nullable = false)
    val userId: UUID,

    @Column(nullable = false, columnDefinition = "text")
    val title: String,

    @Column(nullable = false, columnDefinition = "text")
    val body: String,

    val isRead: Boolean = false,

    val createdAt: Instant = Instant.now(),
)
