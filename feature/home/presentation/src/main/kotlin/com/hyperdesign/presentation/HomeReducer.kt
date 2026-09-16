package com.hyperdesign.presentation

import com.hyperdesign.presentation.mvi.Reducer
import kotlin.collections.copy

class HomeReducer : Reducer<HomeState, HomeIntent> {

    override fun reduce(
        state: HomeState,
        intent: HomeIntent
    ): HomeState =
        when (intent) {

            is HomeIntent.ConnectivityChanged -> state.copy(isOffline = intent.isOffline)
            HomeIntent.Load -> state
            is HomeIntent.OpenDetails -> state
            HomeIntent.Refresh, HomeIntent.Retry -> state.copy(
                isRefreshing = true,
                fatalError = null
            )

            is HomeIntent.RefreshFinished -> state.copy(
                isRefreshing = false,

                fatalError = intent.errorMessage ?: state.fatalError,
            )

            is HomeIntent.ToggleFavorite -> state
        }

}