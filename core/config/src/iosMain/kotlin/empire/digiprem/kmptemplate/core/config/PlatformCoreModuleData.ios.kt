package empire.digiprem.kmptemplate.core.config

import empire.digiprem.kmptemplate.core.data.security.IosCryptoManager
import empire.digiprem.kmptemplate.core.data.utils.createDataStore
import empire.digiprem.kmptemplate.core.domain.service.CryptoManager
import io.ktor.client.engine.darwin.Darwin
import org.koin.dsl.module

actual val platformCoreModuleData = module {
    single<CryptoManager> { IosCryptoManager() }
    single { createDataStore() }
    single { Darwin.create() }
}
