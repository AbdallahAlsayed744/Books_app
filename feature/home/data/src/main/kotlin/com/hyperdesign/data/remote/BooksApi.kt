package com.hyperdesign.data.remote

import com.hyperdesign.data.remote.dto.BookResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class BooksApi(
    private val client: HttpClient,
) {

    suspend fun getBooks(query: String, offset: Int,num: Int): BookResponseDto =
        client.get("search-books") {
            parameter("query",query)
            parameter("number", num)
            parameter("offset", offset)
        }.body()
}