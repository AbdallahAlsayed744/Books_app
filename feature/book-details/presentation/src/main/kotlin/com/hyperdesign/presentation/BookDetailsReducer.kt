package com.hyperdesign.presentation

import com.hyperdesign.presentation.mvi.Reducer

class BookDetailsReducer: Reducer<BookDetailsState, BookDetailsIntent> {

    override fun reduce(
        state: BookDetailsState,
        intent: BookDetailsIntent
    ): BookDetailsState =
        when(intent){
            is BookDetailsIntent.Failed ->  state.copy(isLoading = false, error = intent.message)
            is BookDetailsIntent.FavoriteChanged -> state.copy(isFavorite = intent.isFavorite)
            BookDetailsIntent.Load ->  state.copy(isLoading = true, error = null)
            is BookDetailsIntent.Loaded -> state.copy(isLoading = false, book = intent.movie, error = null)
            BookDetailsIntent.Retry ->  state.copy(isLoading = true, error = null)
            BookDetailsIntent.ToggleFavorite -> state
        }


}