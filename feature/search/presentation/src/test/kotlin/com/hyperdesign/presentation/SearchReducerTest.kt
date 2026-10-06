package com.hyperdesign.presentation

import com.hyperdesign.presentation.model.SearchResultUiModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SearchReducerTest {
    private val reducer = SearchReducer()
    private val initial = SearchState()

    private val results = listOf(
        SearchResultUiModel(id = 1, name = "Tolkien"),
    )

    @Test
    fun `query changed updates query and clears error`() {
        val start = initial.copy(error = "boom")
        val next = reducer.reduce(start, SearchIntent.QueryChanged("tolkien"))

        assertEquals("tolkien", next.query)
        assertNull(next.error)
    }

    @Test
    fun `loading sets the loading flag and clears error`() {
        val start = initial.copy(error = "old error")
        val next = reducer.reduce(start, SearchIntent.Loading)

        assertTrue(next.isLoading)
        assertNull(next.error)
    }

    @Test
    fun `retry sets loading flag and clears error`() {
        val start = initial.copy(error = "old error", isLoading = false)
        val next = reducer.reduce(start, SearchIntent.Retry)

        assertTrue(next.isLoading)
        assertNull(next.error)
    }

    @Test
    fun `results loaded populates results and stops loading`() {
        val start = initial.copy(isLoading = true)
        val next = reducer.reduce(start, SearchIntent.ResultsLoaded(results))

        assertFalse(next.isLoading)
        assertEquals(results, next.results)
        assertNull(next.error)
    }

    @Test
    fun `failed sets the error message and stops loading`() {
        val next = reducer.reduce(initial.copy(isLoading = true), SearchIntent.Failed("nope"))

        assertFalse(next.isLoading)
        assertEquals("nope", next.error)
    }

    @Test
    fun `open details is a no-op`() {
        assertEquals(initial, reducer.reduce(initial, SearchIntent.OpenDetails(1)))
    }

    @Test
    fun `showEmpty is true only for non-blank query with no results and not loading`() {
        val empty = initial.copy(query = "zzz", isLoading = false, results = emptyList())
        assertTrue(empty.showEmpty)
    }

    @Test
    fun `showEmpty is false when query is blank`() {
        val blank = initial.copy(query = "", results = emptyList())
        assertFalse(blank.showEmpty)
    }

    @Test
    fun `showEmpty is false while loading`() {
        val loading = initial.copy(query = "zzz", isLoading = true, results = emptyList())
        assertFalse(loading.showEmpty)
    }

    @Test
    fun `showEmpty is false when results are present`() {
        val withResults = initial.copy(query = "zzz", isLoading = false, results = results)
        assertFalse(withResults.showEmpty)
    }
}
