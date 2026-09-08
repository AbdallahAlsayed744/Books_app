package com.hyperdesign.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book_remote_keys")
data class BookRemoteKeyEntity(
    @PrimaryKey val bookId: Int,
    val prevKey: Int?,
    val nextKey: Int?,
    val lastUpdated: Long,
)
