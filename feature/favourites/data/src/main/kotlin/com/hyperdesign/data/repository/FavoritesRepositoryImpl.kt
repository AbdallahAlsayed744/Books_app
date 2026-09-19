package com.hyperdesign.data.repository
import android.util.Log
import com.hyperdesign.data.error.safeDbCall
import com.hyperdesign.database.dao.FavoriteBookDao
import com.hyperdesign.database.entity.FavoriteBookEntity
import com.hyperdesign.domain.repository.FavoritesRepository
import com.hyperdesign.domain.result.onFailure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl(
    private val dao: FavoriteBookDao,
    private val now: () -> Long = { System.currentTimeMillis() },
) : FavoritesRepository {
    override fun observeFavoriteIds(): Flow<Set<Int>> =
        dao.observeFavoriteIds().map { it.toSet() }

    override suspend fun isFavorite(bookId: Int): Boolean = dao.isFavorite(bookId)

    override suspend fun toggleFavorite(bookId: Int) {
        safeDbCall {
            if (dao.isFavorite(bookId)) {
                dao.remove(bookId)
            } else {
                dao.add(
                    FavoriteBookEntity(
                        bookId = bookId,
                        favoriteAt = now(),
                        pendingSync = true
                    )
                )
            }
        }.onFailure { error ->
            Log.w(TAG, "Failed to toggle favourite for book $bookId: $error")
        }
    }

    private companion object {
        const val TAG = "FavoritesRepository"
    }
}
