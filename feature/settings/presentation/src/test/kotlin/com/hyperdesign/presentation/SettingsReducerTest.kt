package com.hyperdesign.presentation

import com.hyperdesign.contract.preferences.AppLanguage
import com.hyperdesign.contract.preferences.DynamicColor
import com.hyperdesign.contract.preferences.ThemeMode
import com.hyperdesign.contract.preferences.UserPreferences
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SettingsReducerTest {
    private val reducer = SettingsReducer()
    private val initial = SettingsState()

    @Test
    fun `preferences loaded replaces the whole snapshot`() {
        val prefs = UserPreferences(themeMode = ThemeMode.DARK, language = AppLanguage.ARABIC)
        val next = reducer.reduce(initial, SettingsIntent.PreferencesLoaded(prefs))

        assertEquals(prefs, next.preferences)
    }

    @Test
    fun `select theme updates only the theme mode`() {
        val next = reducer.reduce(initial, SettingsIntent.SelectTheme(ThemeMode.DARK))

        assertEquals(ThemeMode.DARK, next.preferences.themeMode)
        assertEquals(initial.preferences.language, next.preferences.language)
        assertEquals(initial.preferences.dynamicColor, next.preferences.dynamicColor)
    }

    @Test
    fun `select language updates only the language`() {
        val next = reducer.reduce(initial, SettingsIntent.SelectLanguage(AppLanguage.ARABIC))

        assertEquals(AppLanguage.ARABIC, next.preferences.language)
        assertEquals(initial.preferences.themeMode, next.preferences.themeMode)
    }

    @Test
    fun `select dynamic color updates only that flag`() {
        val next = reducer.reduce(initial, SettingsIntent.SelectDynamicColor(DynamicColor.DISABLED))

        assertEquals(DynamicColor.DISABLED, next.preferences.dynamicColor)
        assertEquals(initial.preferences.themeMode, next.preferences.themeMode)
        assertEquals(initial.preferences.language, next.preferences.language)
    }
}
