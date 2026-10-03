package com.hyperdesign.data.remote
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchAuthorsDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String = "",
)
@Serializable
data class SearchResponseDto(
    @SerialName("authors") val results: List<SearchAuthorsDto> = emptyList(),
)

class SearchApi(private val client: HttpClient) {
    suspend fun search(name: String): SearchResponseDto =
        client.get("search-authors") { parameter("name", name) }.body()
}
