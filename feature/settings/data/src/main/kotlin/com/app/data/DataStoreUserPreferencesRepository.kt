package com.app.data
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.hyperdesign.contract.preferences.AppLanguage
import com.hyperdesign.contract.preferences.DynamicColor
import com.hyperdesign.contract.preferences.ThemeMode
import com.hyperdesign.contract.preferences.UserPreferences
import com.hyperdesign.contract.preferences.UserPreferencesRepository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class DataStoreUserPreferencesRepository(
    context: Context,
) : UserPreferencesRepository {
    private val store: DataStore<Preferences> = context.dataStore

    override val preferences: Flow<UserPreferences> = store.data.map { prefs ->
        UserPreferences(
            themeMode = prefs[KEY_THEME]?.let { runCatching { ThemeMode.valueOf(it) }.getOrNull() }
                ?: ThemeMode.SYSTEM,
            dynamicColor = prefs[KEY_DYNAMIC]?.let { runCatching { DynamicColor.valueOf(it) }.getOrNull() }
                ?: DynamicColor.ENABLED,
            language = prefs[KEY_LANG]?.let { tag -> AppLanguage.entries.firstOrNull { it.tag == tag } }
                ?: AppLanguage.ENGLISH,

        )
    }

    override suspend fun setThemeMode(mode: ThemeMode) {
        store.edit { it[KEY_THEME] = mode.name }
    }

    override suspend fun setDynamicColor(value: DynamicColor) {
        store.edit { it[KEY_DYNAMIC] = value.name }
    }

    override suspend fun setLanguage(language: AppLanguage) {
        store.edit { it[KEY_LANG] = language.tag }
    }

    private companion object {
        val KEY_THEME = stringPreferencesKey("theme_mode")
        val KEY_DYNAMIC = stringPreferencesKey("dynamic_color")
        val KEY_LANG = stringPreferencesKey("language")
        val KEY_ONBOARDING = booleanPreferencesKey("onboarding_completed")
    }
}
