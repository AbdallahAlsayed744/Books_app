package com.hyperdesign.presentation

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
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
//    private val favoritesProvider: FavoritesProvider,
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
            .map { paging ->
                paging.map { book -> book.toUi(false) }
            }
            .cachedIn(viewModelScope)
//            .combine(favoritesProvider.observeFavoriteIds())


    override fun handleIntent(intent: HomeIntent) {
        when(intent){
            is HomeIntent.ConnectivityChanged ->Unit
            HomeIntent.Load -> Unit
            is HomeIntent.OpenDetails -> TODO()
            HomeIntent.Refresh -> {}
            is HomeIntent.RefreshFinished -> Unit
            HomeIntent.Retry -> {}
            is HomeIntent.ToggleFavorite -> TODO()
        }
    }

    private fun observeConnectivity() {
        viewModelScope.launch {
            connectivityObserver.status.collect { status ->
                sendIntent(HomeIntent.ConnectivityChanged(status == NetworkStatus.UNAVAILABLE))
            }
        }
    }

}