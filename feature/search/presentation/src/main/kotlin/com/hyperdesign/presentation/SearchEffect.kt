package com.hyperdesign.presentation
import com.hyperdesign.presentation.mvi.Effect

sealed interface SearchEffect : Effect {
    data class NavigateToDetails(val bookId: Int) : SearchEffect
}
