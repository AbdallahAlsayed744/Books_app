package com.hyperdesign.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_books")
data class FavoriteBookEntity(
    @PrimaryKey val bookId: Int,
    val favoriteAt: Long,
    val pendingSync: Boolean = true,
)
