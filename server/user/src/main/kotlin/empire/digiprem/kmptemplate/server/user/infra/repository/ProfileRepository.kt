package empire.digiprem.kmptemplate.server.user.infra.repository

import empire.digiprem.kmptemplate.server.user.infra.entity.ProfileEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ProfileRepository : JpaRepository<ProfileEntity, UUID>
