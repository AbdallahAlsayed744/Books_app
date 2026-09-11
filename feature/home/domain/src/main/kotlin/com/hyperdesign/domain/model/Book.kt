package com.hyperdesign.domain.model

data class Book(
    val id: Int,
    val title: String,
    val subtitle: String?,
    val image: String?,
    val authors: List<Author>,
    val ratingAverage: Double?,
)

data class Author(
    val id: Int,
    val name: String,
)