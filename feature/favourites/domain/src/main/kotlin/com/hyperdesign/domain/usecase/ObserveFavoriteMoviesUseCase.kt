package com.hyperdesign.domain.usecase
import com.hyperdesign.contract.books.BookSummary
import com.hyperdesign.contract.books.BooksProvider
import com.hyperdesign.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

class ObserveFavoriteMoviesUseCase(
    private val favoritesRepository: FavoritesRepository,
    private val bookProvider: BooksProvider,
) : FlowUseCase<Unit, List<BookSummary>> {
    override fun invoke(params: Unit): Flow<List<BookSummary>> =
        favoritesRepository.observeFavoriteIds().flatMapLatest { ids ->
            if (ids.isEmpty()) {
                flowOf(emptyList())
            } else {
                combine(ids.map { id -> bookProvider.observeBook(id) }) { movies ->
                    movies.filterNotNull()
                }
            }
        }
}
