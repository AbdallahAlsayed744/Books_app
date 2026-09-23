package com.hyperdesign.presentation.model

import androidx.compose.runtime.Immutable
import com.hyperdesign.contract.books.BookSummary

@Immutable
data class FavoriteUiModel(
    val id: Int,
    val title: String,
    val posterUrl: String,
    val rating: Double,
)


fun BookSummary.toUi(): FavoriteUiModel = FavoriteUiModel(
    id = id,
    title = title,
    posterUrl = posterUrl,
    rating = rating,
)