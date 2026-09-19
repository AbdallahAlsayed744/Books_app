package com.hyperdesign.domain.di
import com.hyperdesign.domain.usecase.ObserveFavoriteMoviesUseCase
import com.hyperdesign.domain.usecase.ToggleFavoriteUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val favoritesDomainModule =
    module {
        factoryOf(::ObserveFavoriteMoviesUseCase)
        factoryOf(::ToggleFavoriteUseCase)
    }
