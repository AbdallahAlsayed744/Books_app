package com.hyperdesign.presentation

import com.hyperdesign.presentation.mvi.Effect

sealed interface HomeEffect : Effect {

    data class NavigateToDetails(val bookId: Int) : HomeEffect

    data class ShowSnackbar(val message: String) : HomeEffect
}