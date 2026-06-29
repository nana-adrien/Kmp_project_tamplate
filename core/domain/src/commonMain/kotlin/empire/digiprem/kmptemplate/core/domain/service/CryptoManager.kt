package empire.digiprem.kmptemplate.core.domain.service

interface CryptoManager {
    fun encrypt(value: String): String
    fun decrypt(value: String): String
    fun remove(key: String)
    fun onCleared()
}
