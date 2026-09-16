package com.hyperdesign.domain.di

import com.hyperdesign.domain.usecase.ObservePagedBooksUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val booksDomainModule = module {
    factoryOf(::ObservePagedBooksUseCase)

}