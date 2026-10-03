package com.hyperdesign.presentation
import com.hyperdesign.presentation.model.SearchResultUiModel
import com.hyperdesign.presentation.mvi.UiState

data class SearchState(
    val query: String = "",
    val isLoading: Boolean = false,
    val results: List<SearchResultUiModel> = emptyList(),
    val error: String? = null,
) : UiState {
    val showEmpty: Boolean get() = !isLoading && error == null && query.isNotBlank() && results.isEmpty()
}
