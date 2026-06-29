package empire.digiprem.kmptemplate.core.domain.util

inline fun <T, E : ResultError, R> Result<T, E>.map(
    transform: (T) -> R,
): Result<R, E> = when (this) {
    is Result.Success -> Result.Success(transform(data))
    is Result.Failure -> Result.Failure(error)
}

inline fun <T, E : ResultError, F : ResultError> Result<T, E>.mapFailure(
    transform: (E) -> F,
): Result<T, F> = when (this) {
    is Result.Success -> Result.Success(data)
    is Result.Failure -> Result.Failure(transform(error))
}

inline fun <T, E : ResultError, R> Result<T, E>.flatMap(
    transform: (T) -> Result<R, E>,
): Result<R, E> = when (this) {
    is Result.Success -> transform(data)
    is Result.Failure -> Result.Failure(error)
}

inline fun <T, E : ResultError> Result<T, E>.onSuccess(
    action: (T) -> Unit,
): Result<T, E> {
    if (this is Result.Success) action(data)
    return this
}

inline fun <T, E : ResultError> Result<T, E>.onFailure(
    action: (E) -> Unit,
): Result<T, E> {
    if (this is Result.Failure) action(error)
    return this
}

fun <T, E : ResultError> Result<T, E>.getOrNull(): T? =
    if (this is Result.Success) data else null

fun <T, E : ResultError> Result<T, E>.isSuccess(): Boolean = this is Result.Success
fun <T, E : ResultError> Result<T, E>.isFailure(): Boolean = this is Result.Failure
