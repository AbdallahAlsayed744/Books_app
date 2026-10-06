package com.hyperdesign.presentation

import com.hyperdesign.presentation.model.FavoriteUiModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class FavoritesReducerTest {
    private val reducer = FavoritesReducer()
    private val initial = FavoritesState()

    private val items = listOf(
        FavoriteUiModel(id = 1, title = "Clean Code", posterUrl = "p", rating = 4.8),
    )

    @Test
    fun `load sets the loading flag`() {
        val next = reducer.reduce(initial.copy(isLoading = false), FavoritesIntent.Load)
        assertTrue(next.isLoading)
    }

    @Test
    fun `items loaded populates items and stops loading`() {
        val next = reducer.reduce(initial, FavoritesIntent.ItemsLoaded(items))

        assertFalse(next.isLoading)
        assertEquals(items, next.items)
    }

    @Test
    fun `isEmpty is true only after loading finishes with no items`() {
        val loaded = reducer.reduce(initial, FavoritesIntent.ItemsLoaded(emptyList()))
        assertTrue(loaded.isEmpty)
        assertFalse(initial.isEmpty) // still loading initially
    }

    @Test
    fun `isEmpty is false when items exist`() {
        val next = reducer.reduce(initial, FavoritesIntent.ItemsLoaded(items))
        assertFalse(next.isEmpty)
    }

    @Test
    fun `remove does not change state`() {
        assertEquals(initial, reducer.reduce(initial, FavoritesIntent.Remove(bookId = 1)))
    }

    @Test
    fun `open details does not change state`() {
        assertEquals(initial, reducer.reduce(initial, FavoritesIntent.OpenDetails(bookId = 1)))
    }
}
