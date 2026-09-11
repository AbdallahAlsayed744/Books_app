package com.hyperdesign.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookResponseDto(
    @SerialName("available")
    val available : Int ? = null,
    @SerialName("number")
    val number : Int ? = null,
    @SerialName("offset")
    val offset : Int ? = null,
    @SerialName("books")
    val books : List<BookDto> ? = null,

)

@Serializable
data class BookDto(
    @SerialName("id")
    val id : Int ? = null,
    @SerialName("title")
    val title : String ? = null,
    @SerialName("subtitle")
    val subtitle : String ? = null,
    @SerialName("image")
    val image : String ? = null,
    @SerialName("authors")
    val authors : List<AuthorDto> ? = null,
    @SerialName("rating")
    val rating : RatingDto ? = null,


)
@Serializable
data class AuthorDto(
    @SerialName("id")
    val id : Int ? = null,
    @SerialName("name")
    val name : String ? = null,
)
@Serializable
data class RatingDto(
    @SerialName("average")
    val average : Double ? = null,
)
