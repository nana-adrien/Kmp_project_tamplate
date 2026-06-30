package empire.digiprem.kmptemplate.server.settings.infra.repository

import empire.digiprem.kmptemplate.server.settings.infra.entity.SettingsEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SettingsRepository : JpaRepository<SettingsEntity, UUID>
