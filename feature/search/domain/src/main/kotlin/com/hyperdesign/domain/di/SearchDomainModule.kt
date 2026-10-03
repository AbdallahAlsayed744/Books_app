package com.hyperdesign.domain.di
import com.hyperdesign.domain.usecase.SearchMoviesUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val searchDomainModule =
    module {
        factoryOf(::SearchMoviesUseCase)
    }
