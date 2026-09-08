package com.hyperdesign.database.convertor

import androidx.room.TypeConverter
import com.hyperdesign.database.entity.AuthorEntity
import kotlinx.serialization.json.Json

class BookConverters {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    @TypeConverter
    fun authorsToJson(authors: List<AuthorEntity>): String {
        return json.encodeToString(authors)
    }

    @TypeConverter
    fun jsonToAuthors(value: String): List<AuthorEntity> {
        return json.decodeFromString(value)
    }
}