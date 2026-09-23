package com.hyperdesign.presentation.di

import com.hyperdesign.navigation.FeatureEntryProvider
import com.hyperdesign.presentation.FavoritesViewModel
import com.hyperdesign.presentation.navigation.FavouritesEntryProvider
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val favouritesPresentationModule = module {

    viewModelOf(::FavoritesViewModel)

    singleOf(::FavouritesEntryProvider) { bind<FeatureEntryProvider>() }
}
