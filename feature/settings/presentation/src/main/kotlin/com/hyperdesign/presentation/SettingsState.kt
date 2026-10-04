package com.hyperdesign.presentation
import com.hyperdesign.contract.preferences.UserPreferences
import com.hyperdesign.presentation.mvi.UiState

data class SettingsState(
    val preferences: UserPreferences = UserPreferences(),
) : UiState
