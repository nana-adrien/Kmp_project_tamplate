package empire.digiprem.kmptemplate.feature.settings.repository

import empire.digiprem.kmptemplate.feature.settings.model.AppSettings
import kotlinx.coroutines.flow.Flow

interface ISettingsRepository {
    fun observeSettings(): Flow<AppSettings>
    suspend fun updateSettings(settings: AppSettings)
    suspend fun clearSettings()
}
