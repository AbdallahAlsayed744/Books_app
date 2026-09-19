package com.hyperdesign.contract.books

import com.hyperdesign.domain.result.Outcome
import kotlinx.coroutines.flow.Flow


data class BookSummary(
    val id: Int,
    val title: String,
    val posterUrl: String,
    val rating: Double,
    val overview: String = "",
    val releaseDate: String = "",
)
interface BooksProvider {

    suspend fun getBook(id: Int): Outcome<BookSummary>

    fun observeBook(id: Int): Flow<BookSummary?>
}