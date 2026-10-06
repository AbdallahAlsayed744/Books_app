package com.hyperdesign.domain.usecase

import com.hyperdesign.domain.model.SearchAuthor
import com.hyperdesign.domain.repository.SearchRepository
import com.hyperdesign.domain.result.Outcome
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SearchMoviesUseCaseTest {

    private class FakeRepo(
        private val result: Outcome<List<SearchAuthor>>,
    ) : SearchRepository {
        var called = false

        override suspend fun searchMovies(query: String): Outcome<List<SearchAuthor>> {
            called = true
            return result
        }
    }

    @Test
    fun `blank query short-circuits without hitting the repository`() = runTest {
        val repo = FakeRepo(Outcome.Success(emptyList()))
        val useCase = SearchMoviesUseCase(repo)

        val result = useCase("   ")

        assertEquals(Outcome.Success(emptyList<SearchAuthor>()), result)
        assertTrue(!repo.called, "repository must not be called for blank query")
    }

    @Test
    fun `empty string short-circuits without hitting the repository`() = runTest {
        val repo = FakeRepo(Outcome.Success(emptyList()))
        val useCase = SearchMoviesUseCase(repo)

        useCase("")

        assertTrue(!repo.called)
    }

    @Test
    fun `non-blank query delegates to repository with trimmed string`() = runTest {
        val expected = listOf(SearchAuthor(1, "Clean Code"))
        val repo = FakeRepo(Outcome.Success(expected))
        val useCase = SearchMoviesUseCase(repo)

        val result = useCase("  clean code  ")

        assertEquals(Outcome.Success(expected), result)
        assertTrue(repo.called)
    }

    @Test
    fun `failure from repository is propagated`() = runTest {
        val repo = FakeRepo(Outcome.Failure(com.hyperdesign.domain.error.AppError.Network))
        val useCase = SearchMoviesUseCase(repo)

        val result = useCase("kotlin")

        assertTrue(result is Outcome.Failure)
    }
}
