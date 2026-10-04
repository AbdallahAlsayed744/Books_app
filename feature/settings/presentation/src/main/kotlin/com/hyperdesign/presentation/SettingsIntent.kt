package com.hyperdesign.presentation
import com.hyperdesign.contract.preferences.AppLanguage
import com.hyperdesign.contract.preferences.DynamicColor
import com.hyperdesign.contract.preferences.ThemeMode
import com.hyperdesign.contract.preferences.UserPreferences
import com.hyperdesign.presentation.mvi.Intent

sealed interface SettingsIntent : Intent {
    data class PreferencesLoaded(val preferences: UserPreferences) : SettingsIntent
    data class SelectTheme(val mode: ThemeMode) : SettingsIntent
    data class SelectLanguage(val language: AppLanguage) : SettingsIntent
    data class SelectDynamicColor(val value: DynamicColor) : SettingsIntent
}
