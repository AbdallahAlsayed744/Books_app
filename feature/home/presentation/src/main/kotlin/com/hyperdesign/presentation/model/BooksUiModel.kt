package com.hyperdesign.presentation.model

import androidx.compose.runtime.Immutable
import com.hyperdesign.domain.model.Author
import com.hyperdesign.domain.model.Book

@Immutable
data class BooksUiModel(
    val id: Int,
    val title: String,
    val authors: List<AuthorUiModel>,
    val description: String,
    val posterUrl: String,
    val ratingLabel: String,
    val isFavorite: Boolean = false,
)

@Immutable
data class AuthorUiModel(
    val name: String,
    val id: Int
)


fun Author.toUi(): AuthorUiModel = AuthorUiModel(
    name = name,
    id = id
)

fun Book.toUi(isFavorite: Boolean = false): BooksUiModel = BooksUiModel(
    id = id,
    title = title,
    posterUrl = image?:"",
    description = subtitle?:"not found" ,
    ratingLabel = ratingAverage.toString(),
    authors = authors.map { it.toUi() },
    isFavorite = isFavorite,
)