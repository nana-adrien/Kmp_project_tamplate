package empire.digiprem.kmptemplate.core.data.networking

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class HttpClientFactory(
    private val engine: HttpClientEngine,
    private val logger: Logger = Logger.withTag("HttpClient"),
) {
    fun create(): HttpClient = HttpClient(engine) {
        installContentNegotiation()
        installLogging()
        installWebSockets()
    }

    private fun HttpClientConfig<*>.installContentNegotiation() {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                coerceInputValues = true
            })
        }
    }

    private fun HttpClientConfig<*>.installLogging() {
        install(Logging) {
            level = LogLevel.BODY
            this.logger = object : io.ktor.client.plugins.logging.Logger {
                override fun log(message: String) = this@HttpClientFactory.logger.d { message }
            }
        }
    }

    private fun HttpClientConfig<*>.installWebSockets() {
        install(WebSockets)
    }
}
