package com.hyperdesign.domain.repository

import androidx.paging.PagingData
import com.hyperdesign.domain.model.Book
import com.hyperdesign.domain.result.Outcome
import kotlinx.coroutines.flow.Flow

interface BookRepository {

    fun pagedBooks(): Flow<PagingData<Book>>

    fun observeBook(id: Int): Flow<Book?>

    suspend fun getBook(id: Int): Outcome<Book>

    suspend fun refresh(): Outcome<Unit>
}