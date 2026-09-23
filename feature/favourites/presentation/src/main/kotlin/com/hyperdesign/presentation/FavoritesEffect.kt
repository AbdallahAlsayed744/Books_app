package com.hyperdesign.presentation

import com.hyperdesign.presentation.mvi.Effect

sealed interface FavoritesEffect : Effect {
    data class NavigateToDetails(val bookId: Int) : FavoritesEffect
}
