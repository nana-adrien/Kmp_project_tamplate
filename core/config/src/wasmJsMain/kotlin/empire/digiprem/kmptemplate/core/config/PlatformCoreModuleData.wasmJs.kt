package empire.digiprem.kmptemplate.core.config

import empire.digiprem.kmptemplate.core.domain.service.CryptoManager
import io.ktor.client.engine.js.Js
import org.koin.dsl.module

actual val platformCoreModuleData = module {
    single<CryptoManager> { WebCryptoManager() }
    // DataStore is not supported on WASM/JS — replace with localStorage or IndexedDB in production
    // single { InMemoryDataStore() }
    single { Js.create() }
}

// Web: no persistent keystore — data is held in memory only
private class WebCryptoManager : CryptoManager {
    private val store = mutableMapOf<String, String>()

    override fun encrypt(value: String): String {
        val key = "web_${value.hashCode()}"
        store[key] = value
        return key
    }

    override fun decrypt(value: String): String = store[value] ?: value
    override fun remove(key: String) { store.remove(key) }
    override fun onCleared() { store.clear() }
}
