package com.hyperdesign.presentation

import app.cash.turbine.test
import com.hyperdesign.contract.books.BookSummary
import com.hyperdesign.contract.books.BooksProvider
import com.hyperdesign.domain.repository.FavoritesRepository
import com.hyperdesign.domain.result.Outcome
import com.hyperdesign.domain.usecase.ObserveFavoriteMoviesUseCase
import com.hyperdesign.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesViewModelTest {

    private class FakeFavoritesRepository(
        private val idsFlow: Flow<Set<Int>> = flowOf(emptySet()),
    ) : FavoritesRepository {
        var toggledId: Int? = null

        override fun observeFavoriteIds(): Flow<Set<Int>> = idsFlow
        override suspend fun isFavorite(bookId: Int): Boolean = false
        override suspend fun toggleFavorite(bookId: Int) {
            toggledId = bookId
        }
    }

    private class FakeBooksProvider(
        private val books: Map<Int, BookSummary> = emptyMap(),
    ) : BooksProvider {
        override suspend fun getBook(id: Int): Outcome<BookSummary> =
            books[id]?.let { Outcome.Success(it) }
                ?: Outcome.Failure(com.hyperdesign.domain.error.AppError.NotFound)

        override fun observeBook(id: Int): Flow<BookSummary?> =
            flowOf(books[id])
    }

    private val book1 = BookSummary(1, "Clean Code", "p1", 4.8)
    private val book2 = BookSummary(2, "Refactoring", "p2", 4.5)

    @AfterEach
    fun tearDown() = Dispatchers.resetMain()

    private fun viewModel(
        ids: Set<Int> = emptySet(),
        books: Map<Int, BookSummary> = emptyMap(),
    ): FavoritesViewModel {
        val idsFlow = flowOf(ids)
        val repo = FakeFavoritesRepository(idsFlow)
        val booksProvider = FakeBooksProvider(books)
        return FavoritesViewModel(
            observeFavorites = ObserveFavoriteMoviesUseCase(repo, booksProvider),
            toggleFavorite = ToggleFavoriteUseCase(repo),
        )
    }

    @Test
    fun `observed favorites are mapped to ui items and dispatched`() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        val vm = viewModel(
            ids = setOf(1, 2),
            books = mapOf(1 to book1, 2 to book2),
        )
        advanceUntilIdle()

        assertEquals(2, vm.state.value.items.size)
        assertFalse(vm.state.value.isLoading)
    }

    @Test
    fun `empty ids yields empty items list`() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        val vm = viewModel(ids = emptySet())
        advanceUntilIdle()

        assertTrue(vm.state.value.items.isEmpty())
    }

    @Test
    fun `open details emits NavigateToDetails effect`() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        val vm = viewModel()

        vm.effect.test {
            vm.sendIntent(FavoritesIntent.OpenDetails(bookId = 42))
            val effect = awaitItem()
            assertTrue(effect is FavoritesEffect.NavigateToDetails)
            assertEquals(42, (effect as FavoritesEffect.NavigateToDetails).bookId)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
