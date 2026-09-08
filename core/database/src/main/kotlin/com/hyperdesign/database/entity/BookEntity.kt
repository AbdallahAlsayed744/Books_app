package com.hyperdesign.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val subtitle: String?,
    val image: String?,
    val ratingAverage: Double?,
    val authors: List<AuthorEntity>,
    val page: Int,
    val positionInPage: Int
)

@Serializable
data class AuthorEntity(
    val id: Int,
    val name: String
)