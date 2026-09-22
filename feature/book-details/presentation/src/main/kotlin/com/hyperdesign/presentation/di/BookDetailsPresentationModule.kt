package com.hyperdesign.presentation.di

import com.hyperdesign.navigation.FeatureEntryProvider
import com.hyperdesign.presentation.BookDetailsViewModel
import com.hyperdesign.presentation.navigation.BookDetailsEntryProvider
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val bookDetailsPresentationModule = module {
    viewModel { (bookId: Int) -> BookDetailsViewModel(bookId, get(), get(), get()) }
    singleOf(::BookDetailsEntryProvider) { bind<FeatureEntryProvider>() }
}

