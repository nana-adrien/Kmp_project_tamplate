package empire.digiprem.kmptemplate.server.notifications.api

import empire.digiprem.kmptemplate.contracts.notifications.NotificationDto
import empire.digiprem.kmptemplate.server.common.util.currentUserId
import empire.digiprem.kmptemplate.server.notifications.service.NotificationsService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/notifications", name = "Notifications")
class NotificationsController(
    private val notificationsService: NotificationsService,
) {

    @GetMapping("/get")
    fun getNotifications(): List<NotificationDto> {
        return notificationsService.getNotifications(currentUserId())
    }

    @PostMapping("/{id}/read")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun markRead(@PathVariable id: String) {
        notificationsService.markRead(currentUserId(), id)
    }

    @PostMapping("/read-all")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun markAllRead() {
        notificationsService.markAllRead(currentUserId())
    }
}
