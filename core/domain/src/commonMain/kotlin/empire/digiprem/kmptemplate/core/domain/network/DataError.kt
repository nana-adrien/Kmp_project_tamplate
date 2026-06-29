package empire.digiprem.kmptemplate.core.domain.network

import empire.digiprem.kmptemplate.core.domain.util.ResultError

sealed interface DataError : ResultError {

    sealed interface Remote : DataError {
        data object NoInternet       : Remote
        data object RequestTimeout   : Remote
        data object Unauthorized     : Remote
        data object Forbidden        : Remote
        data object NotFound         : Remote
        data object Conflict         : Remote
        data object BadRequest       : Remote
        data object TooManyRequests  : Remote
        data object ServerError      : Remote
        data object ServerUnavailable: Remote
        data object PayloadTooLarge  : Remote
        data object Serialization    : Remote
        data object Unknown          : Remote
        data class ServerProcessError(val message: String) : Remote
    }

    sealed interface Local : DataError {
        data object DiskFull : Local
        data object NotFound : Local
        data object Unknown  : Local
    }
}
