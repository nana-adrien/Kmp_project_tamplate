package empire.digiprem.kmptemplate.server.user.infra.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "profiles")
class ProfileEntity(
    @Id
    val userId: UUID,

    @Column(columnDefinition = "text")
    val displayName: String = "",

    @Column(columnDefinition = "text")
    val bio: String? = null,

    @Column(columnDefinition = "text")
    val avatarUrl: String? = null,
)
