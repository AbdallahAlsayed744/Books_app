package com.hyperdesign.presentation

import app.cash.turbine.test
import com.app.domain.usecase.ObservePreferencesUseCase
import com.app.domain.usecase.SetDynamicColorUseCase
import com.app.domain.usecase.SetLanguageUseCase
import com.app.domain.usecase.SetThemeModeUseCase
import com.hyperdesign.contract.preferences.AppLanguage
import com.hyperdesign.contract.preferences.DynamicColor
import com.hyperdesign.contract.preferences.ThemeMode
import com.hyperdesign.contract.preferences.UserPreferences
import com.hyperdesign.contract.preferences.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private class FakePrefsRepo(
        initialPrefs: UserPreferences = UserPreferences(),
    ) : UserPreferencesRepository {
        private val _flow = MutableStateFlow(initialPrefs)
        override val preferences: Flow<UserPreferences> = _flow

        override suspend fun setThemeMode(mode: ThemeMode) {
            _flow.value = _flow.value.copy(themeMode = mode)
        }

        override suspend fun setDynamicColor(value: DynamicColor) {
            _flow.value = _flow.value.copy(dynamicColor = value)
        }

        override suspend fun setLanguage(language: AppLanguage) {
            _flow.value = _flow.value.copy(language = language)
        }
    }

    @AfterEach
    fun tearDown() = Dispatchers.resetMain()

    private fun viewModel(repo: FakePrefsRepo = FakePrefsRepo()) = SettingsViewModel(
        observePreferences = ObservePreferencesUseCase(repo),
        setThemeMode = SetThemeModeUseCase(repo),
        setLanguage = SetLanguageUseCase(repo),
        setDynamicColor = SetDynamicColorUseCase(repo),
    )

    @Test
    fun `initial preferences are loaded into state`() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        val repo = FakePrefsRepo(UserPreferences(themeMode = ThemeMode.DARK))
        val vm = viewModel(repo)
        advanceUntilIdle()

        assertEquals(ThemeMode.DARK, vm.state.value.preferences.themeMode)
    }

    @Test
    fun `select theme updates preferences state`() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        val vm = viewModel()
        advanceUntilIdle()

        vm.sendIntent(SettingsIntent.SelectTheme(ThemeMode.LIGHT))
        advanceUntilIdle()

        assertEquals(ThemeMode.LIGHT, vm.state.value.preferences.themeMode)
    }

    @Test
    fun `select language updates preferences state and emits ApplyLanguage effect`() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        val vm = viewModel()
        advanceUntilIdle()

        vm.effect.test {
            vm.sendIntent(SettingsIntent.SelectLanguage(AppLanguage.ARABIC))
            advanceUntilIdle()

            val effect = awaitItem()
            assertEquals(SettingsEffect.ApplyLanguage("ar"), effect)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `select dynamic color updates preferences state`() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        val vm = viewModel()
        advanceUntilIdle()

        vm.sendIntent(SettingsIntent.SelectDynamicColor(DynamicColor.DISABLED))
        advanceUntilIdle()

        assertEquals(DynamicColor.DISABLED, vm.state.value.preferences.dynamicColor)
    }
}
