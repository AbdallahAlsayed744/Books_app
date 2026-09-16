package com.hyperdesign.domain.usecase

import androidx.paging.PagingData
import com.hyperdesign.domain.model.Book
import com.hyperdesign.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow

class ObservePagedBooksUseCase(
    private val repository: BookRepository,
): FlowUseCase<Unit, PagingData<Book>> {

    override fun invoke(params: Unit): Flow<PagingData<Book>> =  repository.pagedBooks()
}