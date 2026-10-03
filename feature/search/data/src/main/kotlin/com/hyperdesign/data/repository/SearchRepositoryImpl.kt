package com.hyperdesign.data.repository
import com.hyperdesign.data.error.safeApiCall
import com.hyperdesign.data.mapper.toDomain
import com.hyperdesign.data.remote.SearchApi
import com.hyperdesign.domain.model.SearchAuthor
import com.hyperdesign.domain.repository.SearchRepository
import com.hyperdesign.domain.result.Outcome
import com.hyperdesign.domain.result.map

class SearchRepositoryImpl(
    private val api: SearchApi,
) : SearchRepository {
    override suspend fun searchMovies(query: String): Outcome<List<SearchAuthor>> =
        safeApiCall { api.search(query) }.map { response -> response.results.map { it.toDomain() } }
}
