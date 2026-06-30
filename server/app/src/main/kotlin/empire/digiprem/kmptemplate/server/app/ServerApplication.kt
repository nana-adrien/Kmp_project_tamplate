package empire.digiprem.kmptemplate.server.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
    scanBasePackages = [
        "empire.digiprem.kmptemplate.server",
    ]
)
class ServerApplication

fun main(args: Array<String>) {
    runApplication<ServerApplication>(*args)
}
