package empire.digiprem.kmptemplate.server.user.infra.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "password_reset_tokens")
class PasswordResetTokenEntity(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @Column(nullable = false)
    val userId: UUID,

    @Column(nullable = false)
    val hashedToken: String,

    @Column(nullable = false)
    val expiresAt: Instant,
)
