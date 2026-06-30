package empire.digiprem.kmptemplate.server.user.api

import empire.digiprem.kmptemplate.contracts.profile.ProfileDto
import empire.digiprem.kmptemplate.contracts.profile.UpdateProfileRequest
import empire.digiprem.kmptemplate.server.common.util.currentUserId
import empire.digiprem.kmptemplate.server.user.service.ProfileService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/profile")
class ProfileController(
    private val profileService: ProfileService,
) {

    @GetMapping("/")
    fun getProfile(): ProfileDto {
        return profileService.getProfile(currentUserId())
    }

    @PutMapping("/")
    fun updateProfile(@RequestBody request: UpdateProfileRequest): ProfileDto {
        return profileService.updateProfile(currentUserId(), request)
    }
}
