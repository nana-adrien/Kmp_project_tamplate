package empire.digiprem.kmptemplate.core.data.dto

import kotlinx.serialization.Serializable

@Serializable
open class ApiResponse(
    val hasSucceeded: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList(),
)

@Serializable
class ApiResponseWithPayload<T>(
    val payload: T? = null,
) : ApiResponse()

@Serializable
data class ErrorMessage(
    val memberNames: List<String> = emptyList(),
    val errorMessage: String = "",
)
