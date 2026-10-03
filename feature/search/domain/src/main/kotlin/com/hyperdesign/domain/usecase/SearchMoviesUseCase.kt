package com.hyperdesign.domain.usecase
import com.hyperdesign.domain.model.SearchAuthor
import com.hyperdesign.domain.repository.SearchRepository
import com.hyperdesign.domain.result.Outcome

class SearchMoviesUseCase(
    private val repository: SearchRepository,
) : UseCase<String, Outcome<List<SearchAuthor>>> {
    override suspend fun invoke(params: String): Outcome<List<SearchAuthor>> =
        if (params.isBlank()) {
            Outcome.Success(emptyList())
        } else {
            repository.searchMovies(params.trim())
        }
}
