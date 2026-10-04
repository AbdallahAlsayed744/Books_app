package com.app.domain.usecase

import com.hyperdesign.contract.preferences.AppLanguage
import com.hyperdesign.contract.preferences.DynamicColor
import com.hyperdesign.contract.preferences.ThemeMode
import com.hyperdesign.contract.preferences.UserPreferences
import com.hyperdesign.contract.preferences.UserPreferencesRepository
import com.hyperdesign.domain.usecase.FlowUseCase
import com.hyperdesign.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow

class ObservePreferencesUseCase(
    private val repository: UserPreferencesRepository,
) : FlowUseCase<Unit, UserPreferences> {
    override fun invoke(params: Unit): Flow<UserPreferences> = repository.preferences
}

class SetThemeModeUseCase(
    private val repository: UserPreferencesRepository,
) : UseCase<ThemeMode, Unit> {
    override suspend fun invoke(params: ThemeMode) = repository.setThemeMode(params)
}

class SetLanguageUseCase(
    private val repository: UserPreferencesRepository,
) : UseCase<AppLanguage, Unit> {
    override suspend fun invoke(params: AppLanguage) = repository.setLanguage(params)
}

class SetDynamicColorUseCase(
    private val repository: UserPreferencesRepository,
) : UseCase<DynamicColor, Unit> {
    override suspend fun invoke(params: DynamicColor) = repository.setDynamicColor(params)
}
