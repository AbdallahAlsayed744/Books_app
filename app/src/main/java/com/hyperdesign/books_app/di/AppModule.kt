package com.hyperdesign.books_app.di

import com.hyperdesign.data.di.commonDataModule
import com.hyperdesign.networking.di.networkModule
import com.hyperdesign.presentation.di.favouritesPresentationModule
import com.hyperdesign.presentation.di.homePresentationModule
import com.hyperdesign.presentation.di.searchPresentationModule
import com.hyperdesign.presentation.di.settingsPresentationModule
import com.hyperdesign.presentation.resource.AndroidResourceProvider
import com.hyperdesign.presentation.resource.ResourceProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single<ResourceProvider> { AndroidResourceProvider(androidContext()) }
}
val allModules = listOf(
    homePresentationModule,
    searchPresentationModule,
    favouritesPresentationModule,
    settingsPresentationModule,
    networkModule,
    commonDataModule,
    appModule
)