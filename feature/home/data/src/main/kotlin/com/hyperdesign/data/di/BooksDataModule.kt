package com.hyperdesign.data.di

import com.hyperdesign.data.remote.BooksApi
import com.hyperdesign.data.repository.BookRepositoryImpl
import com.hyperdesign.domain.repository.BookRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val booksDataModule = module {

    singleOf(::BooksApi)
    singleOf(::BookRepositoryImpl) { bind<BookRepository>() }
//    singleOf(::MovieProviderImpl) { bind<MovieProvider>() }

}