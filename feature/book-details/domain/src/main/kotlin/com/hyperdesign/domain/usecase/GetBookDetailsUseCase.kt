package com.hyperdesign.domain.usecase

import com.hyperdesign.contract.books.BookSummary
import com.hyperdesign.contract.books.BooksProvider
import com.hyperdesign.domain.result.Outcome

class GetBookDetailsUseCase(
    private val bookProvider: BooksProvider,
): UseCase<Int, Outcome<BookSummary>>
{
    override suspend fun invoke(params: Int): Outcome<BookSummary> =  bookProvider.getBook(params)
}