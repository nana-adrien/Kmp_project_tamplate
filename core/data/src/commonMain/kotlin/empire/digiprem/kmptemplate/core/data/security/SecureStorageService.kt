package empire.digiprem.kmptemplate.core.data.security

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import co.touchlab.kermit.Logger
import empire.digiprem.kmptemplate.core.domain.service.CryptoManager
import empire.digiprem.kmptemplate.core.domain.service.SecureStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

open class SecureStorageService<T, D>(
    private val key: String,
    private val cryptoManager: CryptoManager,
    private val dataStore: DataStore<Preferences>,
    private val serializer: KSerializer<D>,
    private val toDomain: (D) -> T,
    private val toSerializable: (T) -> D,
    private val json: Json = Json { ignoreUnknownKeys = true },
    private val logger: Logger = Logger.withTag("SecureStorage"),
) : SecureStorage<T> {

    private val prefKey = stringPreferencesKey("SECURE_KEY_${key.uppercase()}")

    override fun observeData(): Flow<T?> = dataStore.data
        .map { prefs ->
            prefs[prefKey]?.let { encrypted ->
                val decrypted = cryptoManager.decrypt(encrypted)
                json.decodeFromString(serializer, decrypted).let(toDomain)
            }
        }
        .catch { e ->
            logger.e("Error reading from secure storage", e)
            emit(null)
        }

    override suspend fun getData(): T? = observeData().first()

    override suspend fun set(data: T?) {
        dataStore.edit { prefs ->
            if (data == null) {
                prefs.remove(prefKey)
            } else {
                val serialized = json.encodeToString(serializer, data.let(toSerializable))
                prefs[prefKey] = cryptoManager.encrypt(serialized)
            }
        }
    }

    override suspend fun clear() = set(null)
}
