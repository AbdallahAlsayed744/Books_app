package com.hyperdesign.presentation

import com.hyperdesign.presentation.model.BookDetailsUiModel
import com.hyperdesign.presentation.mvi.UiState

data class BookDetailsState(
    val book: BookDetailsUiModel? = null,
    val isLoading: Boolean = false,
    val isFavorite: Boolean = false,
    val error: String? = null
): UiState
