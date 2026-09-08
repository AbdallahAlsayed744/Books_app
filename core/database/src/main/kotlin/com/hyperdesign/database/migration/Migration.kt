package com.hyperdesign.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {

    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `books` (
                `id` INTEGER NOT NULL,
                `title` TEXT NOT NULL,
                `subtitle` TEXT,
                `image` TEXT,
                `authors` TEXT NOT NULL,
                `rating` REAL,
                `page` INTEGER NOT NULL,
                `positionInPage` INTEGER NOT NULL,
                PRIMARY KEY(`id`)
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `book_remote_keys` (
                `bookId` INTEGER NOT NULL,
                `prevKey` INTEGER,
                `nextKey` INTEGER,
                `lastUpdated` INTEGER NOT NULL,
                PRIMARY KEY(`bookId`)
            )
            """.trimIndent()
        )
    }
}

val BookAppMigrations: Array<Migration> = arrayOf(
    MIGRATION_1_2
)