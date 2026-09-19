package com.hyperdesign.data.di
import com.hyperdesign.contract.favorites.FavoritesProvider
import com.hyperdesign.data.contract.FavoritesProviderImpl
import com.hyperdesign.data.repository.FavoritesRepositoryImpl
import com.hyperdesign.domain.repository.FavoritesRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val favoritesDataModule = module {
    single<FavoritesRepository> { FavoritesRepositoryImpl(dao = get()) }
    singleOf(::FavoritesProviderImpl) { bind<FavoritesProvider>() }
}
