package com.hyperdesign.presentation.di

import com.hyperdesign.navigation.FeatureEntryProvider
import com.hyperdesign.presentation.SettingsViewModel
import com.hyperdesign.presentation.navigation.SettingsEntryProvider
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val settingsPresentationModule = module {
    viewModelOf(::SettingsViewModel)

    singleOf(::SettingsEntryProvider) { bind<FeatureEntryProvider>() }
}
