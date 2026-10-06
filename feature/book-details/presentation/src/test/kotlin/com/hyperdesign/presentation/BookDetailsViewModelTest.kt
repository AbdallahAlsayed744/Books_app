package com.hyperdesign.presentation

import com.hyperdesign.contract.books.BookSummary
import com.hyperdesign.contract.books.BooksProvider
import com.hyperdesign.contract.favorites.FavoritesProvider
import com.hyperdesign.domain.error.AppError
import com.hyperdesign.domain.result.Outcome
import com.hyperdesign.domain.usecase.GetBookDetailsUseCase
import com.hyperdesign.presentation.resource.ResourceProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BookDetailsViewModelTest {

    private class FakeBooksProvider(private val result: Outcome<BookSummary>) : BooksProvider {
        override suspend fun getBook(id: Int): Outcome<BookSummary> = result
        override fun observeBook(id: Int): Flow<BookSummary?> = flowOf(null)
    }

    private class FakeFavorites(private val ids: Set<Int>) : FavoritesProvider {
        override fun observeFavoriteIds(): Flow<Set<Int>> = flowOf(ids)
        override suspend fun toggleFavorite(bookId: Int) = Unit
    }

    private class FakeResources : ResourceProvider {
        override fun getString(resId: Int): String = "error"
        override fun getString(resId: Int, vararg formatArgs: Any): String = "error"
    }

    private val summary = BookSummary(
        id = 1,
        title = "Clean Code",
        posterUrl = "poster.jpg",
        rating = 4.8,
        overview = "A handbook of agile software craftsmanship.",
        releaseDate = "2008-08-01",
    )

    @AfterEach
    fun tearDown() = Dispatchers.resetMain()

    private fun viewModel(
        result: Outcome<BookSummary>,
        favoriteIds: Set<Int> = emptySet(),
    ) = BookDetailsViewModel(
        bookId = 1,
        getBookDetails = GetBookDetailsUseCase(FakeBooksProvider(result)),
        favoritesProvider = FakeFavorites(favoriteIds),
        resources = FakeResources(),
    )

    @Test
    fun `successful load exposes the book and stops loading`() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        val vm = viewModel(Outcome.Success(summary))
        advanceUntilIdle()

        assertNotNull(vm.state.value.book)
        assertFalse(vm.state.value.isLoading)
        assertNull(vm.state.value.error)
    }

    @Test
    fun `failed load surfaces an error and no book`() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        val vm = viewModel(Outcome.Failure(AppError.NotFound))
        advanceUntilIdle()

        assertEquals("error", vm.state.value.error)
        assertNull(vm.state.value.book)
        assertFalse(vm.state.value.isLoading)
    }

    @Test
    fun `favourite status reflects the favorites provider`() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        val vm = viewModel(Outcome.Success(summary), favoriteIds = setOf(1))
        advanceUntilIdle()

        assertTrue(vm.state.value.isFavorite)
    }

    @Test
    fun `not in favorites yields isFavorite false`() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        val vm = viewModel(Outcome.Success(summary), favoriteIds = emptySet())
        advanceUntilIdle()

        assertFalse(vm.state.value.isFavorite)
    }
}
