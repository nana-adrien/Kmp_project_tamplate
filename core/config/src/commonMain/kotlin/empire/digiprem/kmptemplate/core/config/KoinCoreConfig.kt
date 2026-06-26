package empire.digiprem.kmptemplate.core.config

import co.touchlab.kermit.Logger
import empire.digiprem.kmptemplate.core.data.networking.HttpClientFactory
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val coreModuleData = module {
    includes(platformCoreModuleData)

    single {
        Json {
            ignoreUnknownKeys  = true
            isLenient          = true
            coerceInputValues  = true
            encodeDefaults     = false
        }
    }

    single { Logger.withTag("KmpTemplate") }

    single<HttpClient> {
        HttpClientFactory(
            engine = get(),
            logger = get(),
        ).create()
    }
}
