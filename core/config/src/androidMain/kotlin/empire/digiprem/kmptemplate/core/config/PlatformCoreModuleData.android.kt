package empire.digiprem.kmptemplate.core.config

import empire.digiprem.kmptemplate.core.data.security.AndroidCryptoManager
import empire.digiprem.kmptemplate.core.data.utils.createDataStore
import empire.digiprem.kmptemplate.core.domain.service.CryptoManager
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformCoreModuleData = module {
    single<CryptoManager> { AndroidCryptoManager() }
    single { createDataStore(androidContext()) }
    single { OkHttp.create() }
}
