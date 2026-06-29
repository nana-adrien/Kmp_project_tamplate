package empire.digiprem.kmptemplate.feature.settings.usecase

import empire.digiprem.kmptemplate.feature.settings.model.AppSettings
import empire.digiprem.kmptemplate.feature.settings.repository.ISettingsRepository
import kotlinx.coroutines.flow.Flow

class GetSettingsUseCase(private val repository: ISettingsRepository) {
    operator fun invoke(): Flow<AppSettings> = repository.observeSettings()
}
