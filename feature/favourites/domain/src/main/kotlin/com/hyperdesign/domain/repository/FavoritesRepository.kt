package com.hyperdesign.domain.repository
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun observeFavoriteIds(): Flow<Set<Int>>

    suspend fun isFavorite(bookId: Int): Boolean

    suspend fun toggleFavorite(bookId: Int)
}
