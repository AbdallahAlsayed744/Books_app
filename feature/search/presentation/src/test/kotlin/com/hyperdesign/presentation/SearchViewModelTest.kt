package com.hyperdesign.presentation

import com.hyperdesign.domain.error.AppError
import com.hyperdesign.domain.model.SearchAuthor
import com.hyperdesign.domain.repository.SearchRepository
import com.hyperdesign.domain.result.Outcome
import com.hyperdesign.domain.usecase.SearchMoviesUseCase
import com.hyperdesign.presentation.resource.ResourceProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private class FakeRepo(private val result: Outcome<List<SearchAuthor>>) : SearchRepository {
        override suspend fun searchMovies(query: String): Outcome<List<SearchAuthor>> = result
    }

    private class FakeResources : ResourceProvider {
        override fun getString(resId: Int): String = "error"
        override fun getString(resId: Int, vararg formatArgs: Any): String = "error"
    }

    @AfterEach
    fun tearDown() = Dispatchers.resetMain()

    private fun viewModel(result: Outcome<List<SearchAuthor>>) =
        SearchViewModel(SearchMoviesUseCase(FakeRepo(result)), FakeResources())

    @Test
    fun `successful search populates results and clears error`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val vm = viewModel(Outcome.Success(listOf(SearchAuthor(1, "Tolkien"))))

        vm.sendIntent(SearchIntent.QueryChanged("tolkien"))
        advanceUntilIdle()

        assertEquals(1, vm.state.value.results.size)
        assertNull(vm.state.value.error)
        assertFalse(vm.state.value.isLoading)
    }

    @Test
    fun `failed search surfaces a localized error message`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val vm = viewModel(Outcome.Failure(AppError.Network))

        vm.sendIntent(SearchIntent.QueryChanged("kotlin"))
        advanceUntilIdle()

        assertEquals("error", vm.state.value.error)
        assertTrue(vm.state.value.results.isEmpty())
    }

    @Test
    fun `blank query yields empty results without triggering loading`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val vm = viewModel(Outcome.Success(listOf(SearchAuthor(1, "Tolkien"))))

        vm.sendIntent(SearchIntent.QueryChanged("   "))
        advanceUntilIdle()

        assertTrue(vm.state.value.results.isEmpty())
        assertFalse(vm.state.value.isLoading)
    }
}
