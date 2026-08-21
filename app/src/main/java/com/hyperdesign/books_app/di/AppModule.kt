package com.hyperdesign.books_app.di

import com.hyperdesign.presentation.di.favouritesPresentationModule
import com.hyperdesign.presentation.di.homePresentationModule
import com.hyperdesign.presentation.di.searchPresentationModule
import com.hyperdesign.presentation.di.settingsPresentationModule
import org.koin.dsl.module

val appModule = module {
//    single<ResourceProvider> { AndroidResourceProvider(androidContext()) }
}
val allModules = listOf(
    homePresentationModule,
    searchPresentationModule,
    favouritesPresentationModule,
    settingsPresentationModule,
)