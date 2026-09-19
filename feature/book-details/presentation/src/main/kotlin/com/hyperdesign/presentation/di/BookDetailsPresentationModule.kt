package com.hyperdesign.presentation.di

import com.hyperdesign.navigation.FeatureEntryProvider
import com.hyperdesign.presentation.navigation.BookDetailsEntryProvider
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val bookDetailsPresentationModule = module {
//    viewModel { (movieId: Int) -> MovieDetailsViewModel(movieId, get(), get(), get()) }
    singleOf(::BookDetailsEntryProvider) { bind<FeatureEntryProvider>() }
}

