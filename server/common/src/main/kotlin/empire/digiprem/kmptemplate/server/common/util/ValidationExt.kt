package empire.digiprem.kmptemplate.server.common.util

import empire.digiprem.kmptemplate.server.common.exception.ValidationException
import io.konform.validation.Invalid
import io.konform.validation.ValidationResult

fun <T> ValidationResult<T>.orThrow() {
    if (this is Invalid) {
        val messages = errors.map { error ->
            if (error.dataPath.isBlank()) error.message
            else "${error.dataPath.removePrefix(".")}: ${error.message}"
        }
        throw ValidationException(messages)
    }
}
