package com.hyperdesign.data.mapper

import com.hyperdesign.data.remote.SearchAuthorsDto
import com.hyperdesign.domain.model.SearchAuthor

fun SearchAuthorsDto.toDomain(): SearchAuthor = SearchAuthor(
    id = id,
    title = name,
)
