package com.hyperdesign.domain.di

import com.hyperdesign.domain.usecase.GetBookDetailsUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val bookDetailsDomainModule = module {
    factoryOf(::GetBookDetailsUseCase)
}