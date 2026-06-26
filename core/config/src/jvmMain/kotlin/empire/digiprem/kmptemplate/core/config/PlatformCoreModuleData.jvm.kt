package empire.digiprem.kmptemplate.core.config

import empire.digiprem.kmptemplate.core.data.utils.createDataStore
import empire.digiprem.kmptemplate.core.data.utils.DATA_STORE_FILE_NAME
import empire.digiprem.kmptemplate.core.domain.service.CryptoManager
import io.ktor.client.engine.cio.CIO
import org.koin.dsl.module
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

actual val platformCoreModuleData = module {
    single<CryptoManager> { DesktopCryptoManager() }
    single { createDataStore { "${System.getProperty("user.home")}/.kmptemplate/$DATA_STORE_FILE_NAME" } }
    single { CIO.create() }
}

private class DesktopCryptoManager : CryptoManager {
    private val key: SecretKey = KeyGenerator.getInstance("AES").apply { init(256) }.generateKey()
    private val encoder = Base64.getEncoder()
    private val decoder = Base64.getDecoder()

    override fun encrypt(value: String): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val iv   = encoder.encodeToString(cipher.iv)
        val data = encoder.encodeToString(cipher.doFinal(value.toByteArray()))
        return "$iv:$data"
    }

    override fun decrypt(value: String): String {
        val parts  = value.split(":")
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(128, decoder.decode(parts[0])))
        return String(cipher.doFinal(decoder.decode(parts[1])))
    }

    override fun remove(key: String) = Unit
    override fun onCleared() = Unit
}
