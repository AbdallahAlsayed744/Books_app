package com.hyperdesign.presentation
import com.hyperdesign.presentation.model.FavoriteUiModel
import com.hyperdesign.presentation.mvi.UiState

data class FavoritesState(
    val isLoading: Boolean = true,
    val items: List<FavoriteUiModel> = emptyList(),
) : UiState {
    val isEmpty: Boolean get() = !isLoading && items.isEmpty()
}
