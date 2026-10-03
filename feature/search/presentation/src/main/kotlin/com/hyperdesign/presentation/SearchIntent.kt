package com.hyperdesign.presentation
import com.hyperdesign.presentation.model.SearchResultUiModel
import com.hyperdesign.presentation.mvi.Intent

sealed interface SearchIntent : Intent {
    data class QueryChanged(val query: String) : SearchIntent
    data object Retry : SearchIntent
    data class ResultsLoaded(val results: List<SearchResultUiModel>) : SearchIntent
    data class Failed(val message: String) : SearchIntent
    data object Loading : SearchIntent
    data class OpenDetails(val movieId: Int) : SearchIntent
}
