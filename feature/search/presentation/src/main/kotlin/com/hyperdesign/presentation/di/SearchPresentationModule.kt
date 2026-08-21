package com.hyperdesign.presentation.di


import com.hyperdesign.navigation.FeatureEntryProvider
import com.hyperdesign.presentation.navigation.SearchEntryProvider
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val searchPresentationModule = module {
    singleOf(::SearchEntryProvider) { bind<FeatureEntryProvider>() }
}