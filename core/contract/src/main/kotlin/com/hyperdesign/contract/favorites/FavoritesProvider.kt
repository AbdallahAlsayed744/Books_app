package com.hyperdesign.contract.favorites
import kotlinx.coroutines.flow.Flow

interface FavoritesProvider {
    fun observeFavoriteIds(): Flow<Set<Int>>

    suspend fun toggleFavorite(bookId: Int)
}
