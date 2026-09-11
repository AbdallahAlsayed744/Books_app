package com.hyperdesign.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hyperdesign.database.entity.BookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Query("""
        SELECT * FROM books
        ORDER BY page ASC, positionInPage ASC
    """)
    fun pagingSource(): PagingSource<Int, BookEntity>

    @Query("""
        SELECT * FROM books
        WHERE id = :id
        LIMIT 1
    """)
    fun observeById(id: Int): Flow<BookEntity?>

    @Query("""
        SELECT * FROM books
        WHERE id = :id
        LIMIT 1
    """)
    suspend fun findById(id: Int): BookEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(books: List<BookEntity>)

    @Query("DELETE FROM books")
    suspend fun clearAll()
}