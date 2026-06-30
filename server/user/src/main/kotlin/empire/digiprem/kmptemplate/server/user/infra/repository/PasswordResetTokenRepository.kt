package empire.digiprem.kmptemplate.server.user.infra.repository

import empire.digiprem.kmptemplate.server.user.infra.entity.PasswordResetTokenEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PasswordResetTokenRepository : JpaRepository<PasswordResetTokenEntity, UUID> {
    fun findByHashedToken(hashedToken: String): PasswordResetTokenEntity?
    fun deleteByUserId(userId: UUID)
}
