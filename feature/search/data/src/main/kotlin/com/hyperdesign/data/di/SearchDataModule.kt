package com.hyperdesign.data.di
import com.hyperdesign.data.remote.SearchApi
import com.hyperdesign.data.repository.SearchRepositoryImpl
import com.hyperdesign.domain.repository.SearchRepository
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.bind
import org.koin.dsl.module

val searchDataModule = module {
    singleOf(::SearchApi)
    singleOf(::SearchRepositoryImpl) { bind<SearchRepository>() }
}
