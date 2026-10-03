package com.hyperdesign.domain.repository
import com.hyperdesign.domain.model.SearchAuthor
import com.hyperdesign.domain.result.Outcome

interface SearchRepository {
    suspend fun searchMovies(query: String): Outcome<List<SearchAuthor>>
}
