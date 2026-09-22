package com.hyperdesign.presentation.model

import androidx.compose.runtime.Immutable
import com.hyperdesign.contract.books.BookSummary

@Immutable
data class BookDetailsUiModel(
    val id: Int,
    val title: String,
    val posterUrl: String,
    val rating: Double,
    val overview: String = "",
    val releaseDate: String = "",
)


fun BookSummary.toUi(): BookDetailsUiModel = BookDetailsUiModel(
    id = id,
    title = title,
    posterUrl=posterUrl,
    rating=rating,
    overview = overview,
    releaseDate=releaseDate

)