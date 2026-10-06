package com.hyperdesign.presentation

import com.hyperdesign.presentation.model.BookDetailsUiModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class BookDetailsReducerTest {
    private val reducer = BookDetailsReducer()
    private val initial = BookDetailsState()

    private val book = BookDetailsUiModel(
        id = 1,
        title = "Clean Code",
        posterUrl = "poster.jpg",
        rating = 4.8,
        overview = "A handbook of agile software craftsmanship.",
        releaseDate = "2008-08-01",
    )

    @Test
    fun `load sets isLoading and clears error`() {
        val start = initial.copy(isLoading = false, error = "boom")
        val next = reducer.reduce(start, BookDetailsIntent.Load)

        assertTrue(next.isLoading)
        assertNull(next.error)
    }

    @Test
    fun `retry sets isLoading and clears error`() {
        val start = initial.copy(isLoading = false, error = "old error")
        val next = reducer.reduce(start, BookDetailsIntent.Retry)

        assertTrue(next.isLoading)
        assertNull(next.error)
    }

    @Test
    fun `loaded populates book, stops loading and clears error`() {
        val start = initial.copy(isLoading = true)
        val next = reducer.reduce(start, BookDetailsIntent.Loaded(book))

        assertFalse(next.isLoading)
        assertEquals(book, next.book)
        assertNull(next.error)
    }

    @Test
    fun `failed sets error and stops loading`() {
        val start = initial.copy(isLoading = true)
        val next = reducer.reduce(start, BookDetailsIntent.Failed("network error"))

        assertFalse(next.isLoading)
        assertEquals("network error", next.error)
    }

    @Test
    fun `favorite changed updates only the isFavorite flag`() {
        val loaded = reducer.reduce(initial, BookDetailsIntent.Loaded(book))
        val next = reducer.reduce(loaded, BookDetailsIntent.FavoriteChanged(isFavorite = true))

        assertTrue(next.isFavorite)
        assertEquals(book, next.book) // book unchanged
    }

    @Test
    fun `toggle favorite is a no-op in the reducer`() {
        val next = reducer.reduce(initial, BookDetailsIntent.ToggleFavorite)
        assertEquals(initial, next)
    }
}
