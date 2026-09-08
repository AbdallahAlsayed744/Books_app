package com.hyperdesign.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hyperdesign.database.convertor.BookConverters
import com.hyperdesign.database.dao.BookDao
import com.hyperdesign.database.dao.BookRemoteKeyDao
import com.hyperdesign.database.dao.FavoriteBookDao
import com.hyperdesign.database.entity.BookEntity
import com.hyperdesign.database.entity.BookRemoteKeyEntity
import com.hyperdesign.database.entity.FavoriteBookEntity

@Database(
    entities = [
        BookEntity::class,
        BookRemoteKeyEntity::class,
        FavoriteBookEntity::class
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(BookConverters::class)
abstract class BookAppDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao

    abstract fun bookRemoteKeyDao(): BookRemoteKeyDao

    abstract fun favoriteBookDao(): FavoriteBookDao

    companion object {
        const val NAME = "bookapp.db"
    }
}