package empire.digiprem.kmptemplate.server.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

private const val BASE_PKG = "empire.digiprem.kmptemplate.server"

@SpringBootApplication(scanBasePackages = [BASE_PKG])
@EnableJpaRepositories(basePackages = [BASE_PKG])
@EntityScan(basePackages = [BASE_PKG])
class ServerApplication

fun main(args: Array<String>) {
    runApplication<ServerApplication>(*args)
}
