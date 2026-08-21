package com.hyperdesign.presentation.di

import com.hyperdesign.navigation.FeatureEntryProvider
import com.hyperdesign.presentation.navigation.SettingsEntryProvider
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val settingsPresentationModule = module {
    singleOf(::SettingsEntryProvider) { bind<FeatureEntryProvider>() }
}
