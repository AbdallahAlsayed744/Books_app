package com.hyperdesign.presentation

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class HomeReducerTest {
    private val reducer = HomeReducer()
    private val initial = HomeState()

    @Test
    fun `refresh sets isRefreshing and clears fatalError`() {
        val start = initial.copy(fatalError = "boom", isRefreshing = false)
        val next = reducer.reduce(start, HomeIntent.Refresh)

        assertTrue(next.isRefreshing)
        assertNull(next.fatalError)
    }

    @Test
    fun `retry sets isRefreshing and clears fatalError`() {
        val start = initial.copy(fatalError = "boom", isRefreshing = false)
        val next = reducer.reduce(start, HomeIntent.Retry)

        assertTrue(next.isRefreshing)
        assertNull(next.fatalError)
    }

    @Test
    fun `refresh finished clears isRefreshing`() {
        val start = initial.copy(isRefreshing = true)
        val next = reducer.reduce(start, HomeIntent.RefreshFinished(errorMessage = null))

        assertFalse(next.isRefreshing)
    }

    @Test
    fun `refresh finished with error message sets fatalError`() {
        val next = reducer.reduce(initial, HomeIntent.RefreshFinished(errorMessage = "network error"))

        assertEquals("network error", next.fatalError)
    }

    @Test
    fun `connectivity changed offline sets isOffline true`() {
        val next = reducer.reduce(initial, HomeIntent.ConnectivityChanged(isOffline = true))
        assertTrue(next.isOffline)
    }

    @Test
    fun `connectivity changed online sets isOffline false`() {
        val start = initial.copy(isOffline = true)
        val next = reducer.reduce(start, HomeIntent.ConnectivityChanged(isOffline = false))
        assertFalse(next.isOffline)
    }

    @Test
    fun `load is a no-op`() {
        assertEquals(initial, reducer.reduce(initial, HomeIntent.Load))
    }

    @Test
    fun `open details is a no-op`() {
        assertEquals(initial, reducer.reduce(initial, HomeIntent.OpenDetails(bookId = 1)))
    }

    @Test
    fun `toggle favorite is a no-op`() {
        assertEquals(initial, reducer.reduce(initial, HomeIntent.ToggleFavorite(bookId = 1)))
    }
}
