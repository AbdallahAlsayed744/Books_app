package com.hyperdesign.data.contract
import com.hyperdesign.contract.favorites.FavoritesProvider
import com.hyperdesign.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow

internal class FavoritesProviderImpl(
    private val repository: FavoritesRepository,
) : FavoritesProvider {
    override fun observeFavoriteIds(): Flow<Set<Int>> = repository.observeFavoriteIds()
    override suspend fun toggleFavorite(bookId: Int) = repository.toggleFavorite(bookId)
}
