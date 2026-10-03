package com.hyperdesign.presentation.model

import androidx.compose.runtime.Immutable
import com.hyperdesign.domain.model.SearchAuthor

@Immutable
data class SearchResultUiModel(
    val id: Int,
    val name: String,
)

fun SearchAuthor.toUi(): SearchResultUiModel = SearchResultUiModel(
    id = id,
    name = title,
)
