package empire.digiprem.kmptemplate.core.domain.service

import kotlinx.coroutines.flow.Flow

interface SecureStorage<T> {
    fun observeData(): Flow<T?>
    suspend fun getData(): T?
    suspend fun set(data: T?)
    suspend fun clear()
}
