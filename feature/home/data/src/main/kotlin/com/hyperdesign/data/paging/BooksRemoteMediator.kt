package com.hyperdesign.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.hyperdesign.data.error.toAppError
import com.hyperdesign.data.mapper.toEntity
import com.hyperdesign.data.remote.BooksApi
import com.hyperdesign.database.BookAppDatabase
import com.hyperdesign.database.entity.BookEntity
import com.hyperdesign.database.entity.BookRemoteKeyEntity
import com.hyperdesign.domain.error.AppErrorException
import kotlin.collections.map
import kotlin.coroutines.cancellation.CancellationException

@OptIn(ExperimentalPagingApi::class)
class BooksRemoteMediator(
    private val query: String,
    private val api: BooksApi,
    private val database: BookAppDatabase,
    private val pageSize: Int = DEFAULT_PAGE_SIZE,
    private val now: () -> Long = { System.currentTimeMillis() },
) : RemoteMediator<Int, BookEntity>() {

    private val bookDao = database.bookDao()
    private val keyDao = database.bookRemoteKeyDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, BookEntity>,
    ): MediatorResult {

        return try {

            val offset = when (loadType) {
                LoadType.REFRESH -> 0

                LoadType.PREPEND ->
                    return MediatorResult.Success(true)

                LoadType.APPEND -> {
                    val last = keyDao.latest()

                    last?.nextKey
                        ?: return MediatorResult.Success(true)
                }
            }

            val response = api.getBooks(
                query = query,
                num = pageSize,
                offset = offset,
            )

            val books = response.books.orEmpty()

            val nextOffset = if (
                books.isEmpty() ||
                offset + books.size >= (response.available ?: 0)
            ) {
                null
            } else {
                offset + books.size
            }

            val prevOffset = if (offset == 0) {
                null
            } else {
                maxOf(0, offset - pageSize)
            }

            val timestamp = now()

            database.withTransaction {

                if (loadType == LoadType.REFRESH) {
                    bookDao.clearAll()
                    keyDao.clearAll()
                }

                val entities = books.mapIndexed { index, dto ->
                    dto.toEntity(
                        offset = offset,
                        position = index,
                        pageSize = pageSize,
                    )
                }

                bookDao.upsertAll(entities)

                keyDao.upsertAll(
                    entities.map {
                        BookRemoteKeyEntity(
                            bookId = it.id,
                            prevKey = prevOffset,
                            nextKey = nextOffset,
                            lastUpdated = timestamp,
                        )
                    }
                )
            }

            MediatorResult.Success(
                endOfPaginationReached = nextOffset == null
            )

        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            MediatorResult.Error(
                AppErrorException(
                    e.toAppError(),
                    cause = e,
                )
            )
        }
    }

    private companion object {
        const val DEFAULT_PAGE_SIZE = 20
    }
}