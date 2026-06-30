package empire.digiprem.kmptemplate.server.user.infra.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "email_verification_tokens")
class EmailVerificationTokenEntity(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @Column(nullable = false)
    val email: String,

    @Column(nullable = false)
    val token: String,

    @Column(nullable = false)
    val expiresAt: Instant,
)
