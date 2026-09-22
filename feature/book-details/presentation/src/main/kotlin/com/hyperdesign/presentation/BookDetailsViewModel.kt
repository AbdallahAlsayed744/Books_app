package com.hyperdesign.presentation

import androidx.lifecycle.viewModelScope
import com.hyperdesign.contract.favorites.FavoritesProvider
import com.hyperdesign.domain.result.Outcome
import com.hyperdesign.domain.usecase.GetBookDetailsUseCase
import com.hyperdesign.presentation.error.toMessage
import com.hyperdesign.presentation.model.toUi
import com.hyperdesign.presentation.mvi.BaseViewModel
import com.hyperdesign.presentation.resource.ResourceProvider
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class BookDetailsViewModel(
    private val bookId: Int,
    private val getBookDetails: GetBookDetailsUseCase,
    private val favoritesProvider: FavoritesProvider,
    private val resources: ResourceProvider,
): BaseViewModel<BookDetailsState, BookDetailsIntent, BookDetailsEffect>(
    initialState = BookDetailsState(),
    reducer = BookDetailsReducer(),
) {
    init {
        sendIntent(BookDetailsIntent.Load)
        observeFavorite()
    }


    override fun handleIntent(intent: BookDetailsIntent) {
        when (intent) {
            BookDetailsIntent.Load, BookDetailsIntent.Retry -> load()
            BookDetailsIntent.ToggleFavorite ->
                viewModelScope.launch { favoritesProvider.toggleFavorite(bookId) }
            is BookDetailsIntent.Loaded,
            is BookDetailsIntent.Failed,
            is BookDetailsIntent.FavoriteChanged,
                -> Unit
        }
    }

    private fun load() {
        viewModelScope.launch {
            when (val result = getBookDetails(bookId)) {
                is Outcome.Success -> sendIntent(BookDetailsIntent.Loaded(result.data.toUi()))
                is Outcome.Failure ->
                    sendIntent(BookDetailsIntent.Failed(result.error.toMessage(resources)))
            }
        }
    }

    private fun observeFavorite() {
        viewModelScope.launch {
            favoritesProvider.observeFavoriteIds()
                .map { bookId in it }
                .collect { sendIntent(BookDetailsIntent.FavoriteChanged(it)) }
        }
    }

}