package com.hyperdesign.presentation

import androidx.paging.PagingData
import com.hyperdesign.contract.favorites.FavoritesProvider
import com.hyperdesign.domain.connectivity.ConnectivityObserver
import com.hyperdesign.domain.connectivity.NetworkStatus
import com.hyperdesign.domain.model.Book
import com.hyperdesign.domain.repository.BookRepository
import com.hyperdesign.domain.result.Outcome
import com.hyperdesign.domain.usecase.ObservePagedBooksUseCase
import com.hyperdesign.presentation.resource.ResourceProvider
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
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private class FakeBookRepository : BookRepository {
        override fun pagedBooks(): Flow<PagingData<Book>> = flowOf(PagingData.empty())
        override fun observeBook(id: Int): Flow<Book?> = flowOf(null)
        override suspend fun getBook(id: Int): Outcome<Book> =
            Outcome.Failure(com.hyperdesign.domain.error.AppError.NotFound)
        override suspend fun refresh(): Outcome<Unit> = Outcome.Success(Unit)
    }

    private class FakeFavorites : FavoritesProvider {
        override fun observeFavoriteIds(): Flow<Set<Int>> = flowOf(emptySet())
        override suspend fun toggleFavorite(bookId: Int) = Unit
    }

    private class FakeConnectivity(
        private val flow: MutableStateFlow<NetworkStatus>,
    ) : ConnectivityObserver {
        override val status: Flow<NetworkStatus> = flow
        override fun isCurrentlyAvailable(): Boolean = flow.value == NetworkStatus.AVAILABLE
    }

    private class FakeResources : ResourceProvider {
        override fun getString(resId: Int): String = "error"
        override fun getString(resId: Int, vararg formatArgs: Any): String = "error"
    }

    @AfterEach
    fun tearDown() = Dispatchers.resetMain()

    private fun viewModel(
        connectivity: MutableStateFlow<NetworkStatus> = MutableStateFlow(NetworkStatus.AVAILABLE),
    ) = HomeViewModel(
        observePagedBooks = ObservePagedBooksUseCase(FakeBookRepository()),
        favoritesProvider = FakeFavorites(),
        connectivityObserver = FakeConnectivity(connectivity),
        resources = FakeResources(),
    )

    @Test
    fun `losing connectivity marks the state as offline`() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        val connectivity = MutableStateFlow(NetworkStatus.AVAILABLE)
        val vm = viewModel(connectivity)
        advanceUntilIdle()

        connectivity.value = NetworkStatus.UNAVAILABLE
        advanceUntilIdle()

        assertTrue(vm.state.value.isOffline)
    }

    @Test
    fun `restoring connectivity clears the offline flag`() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        val connectivity = MutableStateFlow(NetworkStatus.UNAVAILABLE)
        val vm = viewModel(connectivity)
        advanceUntilIdle()

        connectivity.value = NetworkStatus.AVAILABLE
        advanceUntilIdle()

        assertFalse(vm.state.value.isOffline)
    }
}
