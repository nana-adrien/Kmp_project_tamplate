package empire.digiprem.kmptemplate.server.settings.infra.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "user_settings")
class SettingsEntity(
    @Id
    val userId: UUID,

    @Column(columnDefinition = "text")
    val language: String = "en",

    @Column(columnDefinition = "text")
    val theme: String = "system",

    val notificationsEnabled: Boolean = true,
)
