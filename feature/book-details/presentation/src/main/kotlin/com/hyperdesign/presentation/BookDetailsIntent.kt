package com.hyperdesign.presentation

import com.hyperdesign.presentation.model.BookDetailsUiModel
import com.hyperdesign.presentation.mvi.Intent

sealed interface BookDetailsIntent: Intent {
    data object Load : BookDetailsIntent
    data object Retry : BookDetailsIntent
    data class Loaded(val movie: BookDetailsUiModel) : BookDetailsIntent
    data class Failed(val message: String) : BookDetailsIntent

    data class FavoriteChanged(val isFavorite: Boolean) : BookDetailsIntent

    data object ToggleFavorite : BookDetailsIntent

}