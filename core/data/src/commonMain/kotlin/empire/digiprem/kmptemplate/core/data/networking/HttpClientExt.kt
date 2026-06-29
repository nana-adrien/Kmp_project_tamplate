package empire.digiprem.kmptemplate.core.data.networking

import empire.digiprem.kmptemplate.core.data.dto.ApiResponse
import empire.digiprem.kmptemplate.core.data.dto.ApiResponseWithPayload
import empire.digiprem.kmptemplate.core.domain.network.DataError
import empire.digiprem.kmptemplate.core.domain.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException

suspend inline fun <reified Response : ApiResponse> safeApiCall(
    execute: () -> HttpResponse,
): Result<Response, DataError.Remote> = try {
    val response = execute()
    if (!response.status.isSuccess()) {
        return Result.Failure(response.status.value.toDataError())
    }
    val body = response.body<Response>()
    if (!body.hasSucceeded && body.errorMessages.isNotEmpty()) {
        return Result.Failure(
            DataError.Remote.ServerProcessError(
                body.errorMessages.firstOrNull()?.errorMessage.orEmpty()
            )
        )
    }
    Result.Success(body)
} catch (e: UnresolvedAddressException) {
    Result.Failure(DataError.Remote.NoInternet)
} catch (e: SerializationException) {
    Result.Failure(DataError.Remote.Serialization)
} catch (e: Exception) {
    Result.Failure(DataError.Remote.Unknown)
}

suspend inline fun <reified Body, reified Response : ApiResponse> HttpClient.post(
    route: String,
    body: Body,
): Result<Response, DataError.Remote> = safeApiCall {
    post(HttpConstants.BASE_URL + route) {
        contentType(ContentType.Application.Json)
        setBody(body)
    }
}

suspend inline fun <reified Response : ApiResponse> HttpClient.get(
    route: String,
): Result<Response, DataError.Remote> = safeApiCall {
    get(HttpConstants.BASE_URL + route)
}

suspend inline fun <reified Body, reified Response : ApiResponse> HttpClient.put(
    route: String,
    body: Body,
): Result<Response, DataError.Remote> = safeApiCall {
    put(HttpConstants.BASE_URL + route) {
        contentType(ContentType.Application.Json)
        setBody(body)
    }
}

suspend inline fun <reified Response : ApiResponse> HttpClient.delete(
    route: String,
): Result<Response, DataError.Remote> = safeApiCall {
    delete(HttpConstants.BASE_URL + route)
}

fun Int.toDataError(): DataError.Remote = when (this) {
    400  -> DataError.Remote.BadRequest
    401  -> DataError.Remote.Unauthorized
    403  -> DataError.Remote.Forbidden
    404  -> DataError.Remote.NotFound
    408  -> DataError.Remote.RequestTimeout
    409  -> DataError.Remote.Conflict
    413  -> DataError.Remote.PayloadTooLarge
    429  -> DataError.Remote.TooManyRequests
    in 500..599 -> DataError.Remote.ServerError
    503  -> DataError.Remote.ServerUnavailable
    else -> DataError.Remote.Unknown
}
