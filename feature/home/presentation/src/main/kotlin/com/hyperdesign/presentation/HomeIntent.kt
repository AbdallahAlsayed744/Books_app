package com.hyperdesign.presentation

import com.hyperdesign.presentation.mvi.Intent

sealed interface HomeIntent : Intent {

    data object Load : HomeIntent
    data object Refresh : HomeIntent
    data object Retry : HomeIntent
    data class OpenDetails(val bookId: Int) : HomeIntent
    data class ToggleFavorite(val bookId: Int) : HomeIntent
    data class ConnectivityChanged(val isOffline: Boolean) : HomeIntent
    data class RefreshFinished(val errorMessage: String?) : HomeIntent
}