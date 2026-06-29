package empire.digiprem.kmptemplate.feature.settings.usecase

import empire.digiprem.kmptemplate.feature.settings.model.AppSettings
import empire.digiprem.kmptemplate.feature.settings.repository.ISettingsRepository

class UpdateSettingsUseCase(private val repository: ISettingsRepository) {
    suspend operator fun invoke(settings: AppSettings) = repository.updateSettings(settings)
}
