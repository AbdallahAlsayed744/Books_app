package com.hyperdesign.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hyperdesign.database.entity.BookRemoteKeyEntity

@Dao
interface BookRemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(keys: List<BookRemoteKeyEntity>)

    @Query("""
        SELECT * FROM book_remote_keys
        ORDER BY lastUpdated DESC
        LIMIT 1
    """)
    suspend fun latest(): BookRemoteKeyEntity?

    @Query("SELECT MAX(lastUpdated) FROM book_remote_keys")
    suspend fun lastUpdated(): Long?

    @Query("DELETE FROM book_remote_keys")
    suspend fun clearAll()
}