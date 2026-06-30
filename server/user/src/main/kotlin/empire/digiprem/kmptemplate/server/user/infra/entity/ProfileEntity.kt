package empire.digiprem.kmptemplate.server.user.infra.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "profiles")
class ProfileEntity(
    @Id
    val userId: UUID,

    val displayName: String = "",

    val bio: String? = null,

    val avatarUrl: String? = null,
)
