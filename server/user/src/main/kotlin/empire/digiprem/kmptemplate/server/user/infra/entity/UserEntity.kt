package empire.digiprem.kmptemplate.server.user.infra.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "users")
class UserEntity(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @Column(unique = true, nullable = false, columnDefinition = "text")
    val email: String,

    @Column(unique = true, nullable = false, columnDefinition = "text")
    val username: String,

    @Column(nullable = false, columnDefinition = "text")
    val hashedPassword: String,

    val hasVerifiedEmail: Boolean = false,

    val createdAt: Instant = Instant.now(),
)
