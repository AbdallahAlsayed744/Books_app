package com.hyperdesign.data.contract

import com.hyperdesign.contract.books.BookSummary
import com.hyperdesign.contract.books.BooksProvider
import com.hyperdesign.data.mapper.toSummary
import com.hyperdesign.domain.repository.BookRepository
import com.hyperdesign.domain.result.Outcome
import com.hyperdesign.domain.result.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BooksProviderImpl(
    private val bookRepo: BookRepository
): BooksProvider {
    override suspend fun getBook(id: Int): Outcome<BookSummary> =
        bookRepo.getBook(id).map { it.toSummary() }

    override fun observeBook(id: Int): Flow<BookSummary?> =
        bookRepo.observeBook(id).map { it?.toSummary() }
}