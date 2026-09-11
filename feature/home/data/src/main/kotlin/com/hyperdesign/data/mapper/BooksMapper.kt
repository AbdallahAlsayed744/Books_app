package com.hyperdesign.data.mapper

import android.R.attr.description
import com.hyperdesign.data.remote.dto.BookDto
import com.hyperdesign.database.entity.AuthorEntity
import com.hyperdesign.database.entity.BookEntity
import com.hyperdesign.domain.model.Author
import com.hyperdesign.domain.model.Book

fun BookDto.toEntity(
    offset: Int,
    position: Int,
    pageSize: Int,
): BookEntity = BookEntity(
    id = id ?: 0,
    title = title.orEmpty(),
    subtitle = subtitle,
    image = image,
    ratingAverage = rating?.average ?: 0.0,
    authors = authors.orEmpty().map { author ->
        AuthorEntity(
            id = author.id ?: 0,
            name = author.name.orEmpty(),
        )
    },
    page = (offset / pageSize) + 1,
    positionInPage = position,
)

fun BookEntity.toDomain(): Book = Book(
    id = id,
    title = title,
    authors = authors.map { author -> Author(id = author.id, name = author.name) },
    subtitle = subtitle,
    image = image,
    ratingAverage = ratingAverage,
)