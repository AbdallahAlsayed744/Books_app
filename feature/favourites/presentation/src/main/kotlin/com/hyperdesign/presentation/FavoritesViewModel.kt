package com.hyperdesign.presentation
import androidx.lifecycle.viewModelScope
import com.hyperdesign.domain.usecase.ObserveFavoriteMoviesUseCase
import com.hyperdesign.domain.usecase.ToggleFavoriteUseCase
import com.hyperdesign.presentation.model.toUi
import com.hyperdesign.presentation.mvi.BaseViewModel
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val observeFavorites: ObserveFavoriteMoviesUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
) : BaseViewModel<FavoritesState, FavoritesIntent, FavoritesEffect>(
    initialState = FavoritesState(),
    reducer = FavoritesReducer(),
) {
    init {
        observe()
    }

    override fun handleIntent(intent: FavoritesIntent) {
        when (intent) {
            FavoritesIntent.Load -> Unit
            is FavoritesIntent.Remove ->
                viewModelScope.launch { toggleFavorite(intent.bookId) }
            is FavoritesIntent.OpenDetails ->
                sendEffect(FavoritesEffect.NavigateToDetails(intent.bookId))
            is FavoritesIntent.ItemsLoaded -> Unit
        }
    }

    private fun observe() {
        viewModelScope.launch {
            observeFavorites(Unit).collect { summaries ->
                sendIntent(FavoritesIntent.ItemsLoaded(summaries.map { it.toUi() }))
            }
        }
    }
}
