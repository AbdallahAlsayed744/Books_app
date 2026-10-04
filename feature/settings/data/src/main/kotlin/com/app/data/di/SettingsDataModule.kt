package com.app.data.di
import com.app.data.DataStoreUserPreferencesRepository
import com.hyperdesign.contract.preferences.UserPreferencesRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val settingsDataModule = module {
    single<UserPreferencesRepository> { DataStoreUserPreferencesRepository(androidContext()) }
}
