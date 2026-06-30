package empire.digiprem.kmptemplate.server.settings.infra.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "user_settings")
class SettingsEntity(
    @Id
    val userId: UUID,

    val language: String = "en",

    val theme: String = "system",

    val notificationsEnabled: Boolean = true,
)
