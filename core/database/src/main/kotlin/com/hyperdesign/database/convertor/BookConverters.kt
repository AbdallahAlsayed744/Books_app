package com.hyperdesign.database.convertor

import androidx.room.TypeConverter
import com.hyperdesign.database.entity.AuthorEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val converterJson = Json { ignoreUnknownKeys = true }

// Inline reified wrappers — resolved at compile time, invisible to KSP/Room processing
private inline fun <reified T> encode(value: T): String = converterJson.encodeToString(value)
private inline fun <reified T> decode(value: String): T = converterJson.decodeFromString(value)

class BookConverters {

    @TypeConverter
    fun authorsToJson(authors: List<AuthorEntity>): String = encode(authors)

    @TypeConverter
    fun jsonToAuthors(value: String): List<AuthorEntity> = decode(value)
}
