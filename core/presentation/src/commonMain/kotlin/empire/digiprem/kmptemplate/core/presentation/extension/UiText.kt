package empire.digiprem.kmptemplate.core.presentation.extension

import org.jetbrains.compose.resources.StringResource

sealed interface UiText {
    data class DynamicString(val value: String) : UiText
    data class Resource(
        val id: StringResource,
        val args: Array<Any> = emptyArray(),
    ) : UiText {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Resource) return false
            return id == other.id && args.contentEquals(other.args)
        }
        override fun hashCode(): Int = 31 * id.hashCode() + args.contentHashCode()
    }
}
