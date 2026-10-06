package com.hyperdesign.presentation.di

import com.hyperdesign.navigation.FeatureEntryProvider
import com.hyperdesign.presentation.HomeViewModel
import com.hyperdesign.presentation.navigation.HomeEntryProvider
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val homePresentationModule = module {
    singleOf(::HomeEntryProvider) { bind<FeatureEntryProvider>() }

    viewModelOf(::HomeViewModel)
}
