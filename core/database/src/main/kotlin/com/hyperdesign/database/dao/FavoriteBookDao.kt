package com.hyperdesign.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hyperdesign.database.entity.FavoriteBookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteBookDao {

    @Query("SELECT bookId FROM favorite_books")
    fun observeFavoriteIds(): Flow<List<Int>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_books WHERE bookId = :bookId)")
    suspend fun isFavorite(bookId: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(favorite: FavoriteBookEntity)

    @Query("DELETE FROM favorite_books WHERE bookId = :bookId")
    suspend fun remove(bookId: Int)

    @Query("SELECT * FROM favorite_books WHERE pendingSync = 1")
    suspend fun pendingSync(): List<FavoriteBookEntity>

}