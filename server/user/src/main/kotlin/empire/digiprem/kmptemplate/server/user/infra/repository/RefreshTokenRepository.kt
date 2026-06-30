package empire.digiprem.kmptemplate.server.user.infra.repository

import empire.digiprem.kmptemplate.server.user.infra.entity.RefreshTokenEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface RefreshTokenRepository : JpaRepository<RefreshTokenEntity, UUID> {
    fun findByUserIdAndHashedToken(userId: UUID, hashedToken: String): RefreshTokenEntity?
    fun deleteByUserIdAndHashedToken(userId: UUID, hashedToken: String)
}
