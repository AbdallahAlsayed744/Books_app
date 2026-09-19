package com.hyperdesign.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.hyperdesign.data.error.safeApiCall
import com.hyperdesign.data.mapper.toDomain
import com.hyperdesign.data.mapper.toEntity
import com.hyperdesign.data.paging.BooksRemoteMediator
import com.hyperdesign.data.remote.BooksApi
import com.hyperdesign.database.BookAppDatabase
import com.hyperdesign.domain.model.Book
import com.hyperdesign.domain.repository.BookRepository
import com.hyperdesign.domain.result.Outcome
import com.hyperdesign.domain.result.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalPagingApi::class)
class BookRepositoryImpl(
    private val api: BooksApi,
    private val database: BookAppDatabase,
) : BookRepository {

    private val bookDao = database.bookDao()


    override fun pagedBooks(): Flow<PagingData<Book>> = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
        remoteMediator = BooksRemoteMediator(query = "books+about+wizards", api = api, database = database),
        pagingSourceFactory = { bookDao.pagingSource() },
    ).flow.map { pagingData ->
        pagingData.map { bookEntity ->
            bookEntity.toDomain()
        }
    }

    override fun observeBook(id: Int): Flow<Book?> =
        bookDao.observeById(id).map { it?.toDomain() }

    override suspend fun getBook(id: Int): Outcome<Book> {
        bookDao.findById(id)?.let {
            return Outcome.Success(it.toDomain())
        }

        return safeApiCall { api.getBookById(id) }.map { dto ->
            val entity = dto.toEntity(offset = 0, pageSize = 0, position = 0)
            bookDao.upsertAll(listOf(entity))
            entity.toDomain()
        }
    }

    override suspend fun refresh(): Outcome<Unit> = safeApiCall {
        val response = api.getBooks(query = "books+about+wizards",0, 10)
        val entities = response.books?.flatten()?.mapIndexed { i, dto -> dto.toEntity(0+i, 1, i) }

        entities?.let { bookDao.upsertAll(it) }
    }

    private companion object {
        const val PAGE_SIZE = 20
    }
}