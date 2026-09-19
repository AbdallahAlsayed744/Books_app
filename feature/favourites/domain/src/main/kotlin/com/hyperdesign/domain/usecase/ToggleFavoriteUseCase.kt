package com.hyperdesign.domain.usecase

import com.hyperdesign.domain.repository.FavoritesRepository

class ToggleFavoriteUseCase(
    private val repository: FavoritesRepository,
) : UseCase<Int, Unit> {
    override suspend fun invoke(params: Int) = repository.toggleFavorite(params)
}
