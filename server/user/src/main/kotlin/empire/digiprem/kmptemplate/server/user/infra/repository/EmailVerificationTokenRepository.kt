package empire.digiprem.kmptemplate.server.user.infra.repository

import empire.digiprem.kmptemplate.server.user.infra.entity.EmailVerificationTokenEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface EmailVerificationTokenRepository : JpaRepository<EmailVerificationTokenEntity, UUID> {
    fun findByToken(token: String): EmailVerificationTokenEntity?
    fun deleteByEmail(email: String)
}
