package com.hyperdesign.presentation
import androidx.lifecycle.viewModelScope
import com.app.domain.usecase.ObservePreferencesUseCase
import com.app.domain.usecase.SetDynamicColorUseCase
import com.app.domain.usecase.SetLanguageUseCase
import com.app.domain.usecase.SetThemeModeUseCase
import com.hyperdesign.presentation.mvi.BaseViewModel
import kotlinx.coroutines.launch

class SettingsViewModel(
    observePreferences: ObservePreferencesUseCase,
    private val setThemeMode: SetThemeModeUseCase,
    private val setLanguage: SetLanguageUseCase,
    private val setDynamicColor: SetDynamicColorUseCase,
) : BaseViewModel<SettingsState, SettingsIntent, SettingsEffect>(
    initialState = SettingsState(),
    reducer = SettingsReducer(),
) {
    init {
        viewModelScope.launch {
            observePreferences(Unit).collect { sendIntent(SettingsIntent.PreferencesLoaded(it)) }
        }
    }

    override fun handleIntent(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.SelectTheme ->
                viewModelScope.launch { setThemeMode(intent.mode) }
            is SettingsIntent.SelectDynamicColor ->
                viewModelScope.launch { setDynamicColor(intent.value) }
            is SettingsIntent.SelectLanguage -> {
                viewModelScope.launch { setLanguage(intent.language) }
                sendEffect(SettingsEffect.ApplyLanguage(intent.language.tag))
            }
            is SettingsIntent.PreferencesLoaded -> Unit
        }
    }
}
