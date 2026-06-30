package empire.digiprem.kmptemplate.server.settings.service

import empire.digiprem.kmptemplate.contracts.settings.SettingsDto
import empire.digiprem.kmptemplate.contracts.settings.UpdateSettingsRequest
import empire.digiprem.kmptemplate.server.settings.infra.entity.SettingsEntity
import empire.digiprem.kmptemplate.server.settings.infra.repository.SettingsRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class SettingsService(
    private val settingsRepository: SettingsRepository,
) {

    fun getSettings(userId: String): SettingsDto {
        val uuid = UUID.fromString(userId)
        val settings = settingsRepository.findById(uuid).orElseGet {
            settingsRepository.save(SettingsEntity(userId = uuid))
        }
        return settings.toDto()
    }

    @Transactional
    fun updateSettings(userId: String, request: UpdateSettingsRequest): SettingsDto {
        val uuid = UUID.fromString(userId)
        val existing = settingsRepository.findById(uuid).orElseGet {
            SettingsEntity(userId = uuid)
        }
        val updated = SettingsEntity(
            userId = existing.userId,
            language = request.language ?: existing.language,
            theme = request.theme ?: existing.theme,
            notificationsEnabled = request.notificationsEnabled ?: existing.notificationsEnabled,
        )
        return settingsRepository.save(updated).toDto()
    }

    private fun SettingsEntity.toDto() = SettingsDto(
        language = language,
        theme = theme,
        notificationsEnabled = notificationsEnabled,
    )
}
