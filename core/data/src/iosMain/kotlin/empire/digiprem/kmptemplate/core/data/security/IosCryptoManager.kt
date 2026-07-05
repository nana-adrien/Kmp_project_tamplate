package empire.digiprem.kmptemplate.core.data.security

import empire.digiprem.kmptemplate.core.domain.service.CryptoManager
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.toCValues
import kotlinx.cinterop.value
import platform.CoreFoundation.CFDictionaryAddValue
import platform.CoreFoundation.CFDictionaryCreateMutable
import platform.CoreFoundation.CFRelease
import platform.CoreFoundation.CFTypeRefVar
import platform.CoreFoundation.kCFBooleanTrue
import platform.Foundation.CFBridgingRelease
import platform.Foundation.CFBridgingRetain
import platform.Foundation.NSData
import platform.Foundation.NSString
import platform.Foundation.NSUUID
import platform.Foundation.stringWithString
import platform.Security.SecItemAdd
import platform.Security.SecItemCopyMatching
import platform.Security.SecItemDelete
import platform.Security.errSecSuccess
import platform.Security.kSecAttrAccessible
import platform.Security.kSecAttrAccessibleWhenUnlocked
import platform.Security.kSecAttrAccount
import platform.Security.kSecAttrService
import platform.Security.kSecClass
import platform.Security.kSecClassGenericPassword
import platform.Security.kSecMatchLimit
import platform.Security.kSecMatchLimitOne
import platform.Security.kSecReturnData
import platform.Security.kSecValueData

@OptIn(ExperimentalForeignApi::class)
class IosCryptoManager : CryptoManager {

    override fun encrypt(value: String): String {
        val uniqueKey = "${KEY_ALIAS}_${NSUUID().UUIDString()}"
        saveToKeychain(uniqueKey, value.encodeToByteArray())
        return uniqueKey
    }

    override fun decrypt(value: String): String = loadFromKeychain(value) ?: value

    override fun remove(key: String) {
        val query = CFDictionaryCreateMutable(null, 0, null, null)!!
        CFDictionaryAddValue(query, kSecClass, kSecClassGenericPassword)
        CFDictionaryAddValue(query, kSecAttrAccount, CFBridgingRetain(NSString.stringWithString(key)))
        SecItemDelete(query)
        CFRelease(query)
    }

    override fun onCleared() {
        val query = CFDictionaryCreateMutable(null, 0, null, null)!!
        CFDictionaryAddValue(query, kSecClass, kSecClassGenericPassword)
        CFDictionaryAddValue(query, kSecAttrService, CFBridgingRetain(NSString.stringWithString(KEY_ALIAS)))
        SecItemDelete(query)
        CFRelease(query)
    }

    private fun saveToKeychain(key: String, data: ByteArray) {
        val nsData = data.toNSData()
        val query  = CFDictionaryCreateMutable(null, 0, null, null)!!
        CFDictionaryAddValue(query, kSecClass,          kSecClassGenericPassword)
        CFDictionaryAddValue(query, kSecAttrAccount,    CFBridgingRetain(NSString.stringWithString(key)))
        CFDictionaryAddValue(query, kSecAttrService,    CFBridgingRetain(NSString.stringWithString(KEY_ALIAS)))
        CFDictionaryAddValue(query, kSecValueData,      CFBridgingRetain(nsData))
        CFDictionaryAddValue(query, kSecAttrAccessible, kSecAttrAccessibleWhenUnlocked)
        SecItemDelete(query)
        SecItemAdd(query, null)
        CFRelease(query)
    }

    private fun loadFromKeychain(key: String): String? {
        val query = CFDictionaryCreateMutable(null, 0, null, null)!!
        CFDictionaryAddValue(query, kSecClass,       kSecClassGenericPassword)
        CFDictionaryAddValue(query, kSecAttrAccount, CFBridgingRetain(NSString.stringWithString(key)))
        CFDictionaryAddValue(query, kSecReturnData,  kCFBooleanTrue)
        CFDictionaryAddValue(query, kSecMatchLimit,  kSecMatchLimitOne)
        return memScoped {
            val result = alloc<CFTypeRefVar>()
            val status = SecItemCopyMatching(query, result.ptr)
            CFRelease(query)
            if (status != errSecSuccess) return@memScoped null
            val nsData = CFBridgingRelease(result.value) as? NSData ?: return@memScoped null
            nsData.toByteArray().decodeToString()
        }
    }

    private fun ByteArray.toNSData(): NSData = NSData.create(
        bytes = this.toCValues().ptr,
        length = size.toULong()
    )

    private fun NSData.toByteArray(): ByteArray {
        val bytes = this.bytes ?: return ByteArray(0)
        return ByteArray(this.length.toInt()) { i ->
            (bytes.reinterpret<kotlinx.cinterop.ByteVar>() + i)!!.pointed.value
        }
    }

    companion object {
        private const val KEY_ALIAS = "AppTemplate_Key"
    }
}
