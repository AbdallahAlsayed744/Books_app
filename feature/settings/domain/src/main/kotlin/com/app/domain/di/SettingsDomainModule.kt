package com.app.domain.di
import com.app.domain.usecase.ObservePreferencesUseCase
import com.app.domain.usecase.SetDynamicColorUseCase
import com.app.domain.usecase.SetLanguageUseCase
import com.app.domain.usecase.SetThemeModeUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val settingsDomainModule =
    module {
        factoryOf(::ObservePreferencesUseCase)
        factoryOf(::SetThemeModeUseCase)
        factoryOf(::SetLanguageUseCase)
        factoryOf(::SetDynamicColorUseCase)
    }
