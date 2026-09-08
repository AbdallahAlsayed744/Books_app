package com.hyperdesign.database.di

import androidx.room.Room
import com.hyperdesign.database.BookAppDatabase
import com.hyperdesign.database.migration.BookAppMigrations
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            BookAppDatabase::class.java,
            BookAppDatabase.NAME,
        ).addMigrations(*BookAppMigrations)
            .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
            .build()
    }
    single { get<BookAppDatabase>().bookDao() }
    single { get<BookAppDatabase>().bookRemoteKeyDao() }
    single { get<BookAppDatabase>().favoriteBookDao() }
}