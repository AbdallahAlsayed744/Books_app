package com.hyperdesign.presentation

import com.hyperdesign.presentation.model.FavoriteUiModel
import com.hyperdesign.presentation.mvi.Intent


sealed interface FavoritesIntent : Intent {
    data object Load : FavoritesIntent
    data class ItemsLoaded(val items: List<FavoriteUiModel>) : FavoritesIntent
    data class Remove(val bookId: Int) : FavoritesIntent
    data class OpenDetails(val bookId: Int) : FavoritesIntent
}
