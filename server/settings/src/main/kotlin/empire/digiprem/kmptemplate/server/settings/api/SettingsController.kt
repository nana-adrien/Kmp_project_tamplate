package empire.digiprem.kmptemplate.server.settings.api

import empire.digiprem.kmptemplate.contracts.settings.SettingsDto
import empire.digiprem.kmptemplate.contracts.settings.UpdateSettingsRequest
import empire.digiprem.kmptemplate.server.common.util.currentUserId
import empire.digiprem.kmptemplate.server.settings.service.SettingsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/settings")
class SettingsController(
    private val settingsService: SettingsService,
) {

    @GetMapping("/")
    fun getSettings(): SettingsDto {
        return settingsService.getSettings(currentUserId())
    }

    @PutMapping("/")
    fun updateSettings(@RequestBody request: UpdateSettingsRequest): SettingsDto {
        return settingsService.updateSettings(currentUserId(), request)
    }
}
