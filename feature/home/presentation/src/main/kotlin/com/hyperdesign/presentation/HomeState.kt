package com.hyperdesign.presentation

import com.hyperdesign.presentation.mvi.UiState

data class HomeState(
    val isRefreshing: Boolean = false,
    val isOffline: Boolean = false,
    val fatalError: String? = null,
): UiState
