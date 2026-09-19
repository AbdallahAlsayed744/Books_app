package com.hyperdesign.presentation

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.hyperdesign.contract.favorites.FavoritesProvider
import com.hyperdesign.domain.connectivity.ConnectivityObserver
import com.hyperdesign.domain.connectivity.NetworkStatus
import com.hyperdesign.domain.usecase.ObservePagedBooksUseCase
import com.hyperdesign.presentation.model.BooksUiModel
import com.hyperdesign.presentation.model.toUi
import com.hyperdesign.presentation.mvi.BaseViewModel
import com.hyperdesign.presentation.resource.ResourceProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class HomeViewModel(
    observePagedBooks: ObservePagedBooksUseCase,
//    private val refreshMovies: RefreshMoviesUseCase,
    private val favoritesProvider: FavoritesProvider,
    private val connectivityObserver: ConnectivityObserver,
    private val resources: ResourceProvider,
) : BaseViewModel<HomeState, HomeIntent, HomeEffect>(
    reducer = HomeReducer(),
    initialState = HomeState()
) {

    init {
        observeConnectivity()
    }


    val pagedBooks: Flow<PagingData<BooksUiModel>> =
         observePagedBooks(Unit)
            .cachedIn(viewModelScope)
            .combine(favoritesProvider.observeFavoriteIds()){paging,ids->
                paging.map { book -> book.toUi(book.id in ids) }

            }


    override fun handleIntent(intent: HomeIntent) {
        when(intent){
            is HomeIntent.ConnectivityChanged ->Unit
            HomeIntent.Load -> Unit
            is HomeIntent.OpenDetails -> sendEffect(HomeEffect.NavigateToDetails(intent.bookId))
            HomeIntent.Refresh -> {}
            is HomeIntent.RefreshFinished -> Unit
            HomeIntent.Retry -> {}
            is HomeIntent.ToggleFavorite -> toggleFavorite(intent.bookId)
        }
    }

    private fun refresh() {
//        viewModelScope.launch {
//            when (val result = refreshMovies(selectedCategory.value)) {
//                is Outcome.Success -> sendIntent(MoviesListIntent.RefreshFinished(null))
//                is Outcome.Failure -> {
//                    sendIntent(MoviesListIntent.RefreshFinished(null))
//                    sendEffect(MoviesListEffect.ShowSnackbar(result.error.toMessage(resources)))
//                }
//            }
//        }
    }
    private fun toggleFavorite(bookId: Int) {
        viewModelScope.launch { favoritesProvider.toggleFavorite(bookId) }
    }

    private fun observeConnectivity() {
        viewModelScope.launch {
            connectivityObserver.status.collect { status ->
                sendIntent(HomeIntent.ConnectivityChanged(status == NetworkStatus.UNAVAILABLE))
            }
        }
    }

}