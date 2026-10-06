package com.hyperdesign.domain.result

import com.hyperdesign.domain.error.AppError
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class OutcomeTest {

    @Test
    fun `map transforms success data`() {
        val result = Outcome.Success(2).map { it * 10 }
        assertEquals(Outcome.Success(20), result)
    }

    @Test
    fun `map preserves failure`() {
        val failure: Outcome<Int> = Outcome.Failure(AppError.Network)
        val mapped = failure.map { it * 10 }
        assertEquals(Outcome.Failure(AppError.Network), mapped)
    }

    @Test
    fun `getOrNull returns value on success`() {
        assertEquals(42, Outcome.Success(42).getOrNull())
    }

    @Test
    fun `getOrNull returns null on failure`() {
        val failure: Outcome<Int> = Outcome.Failure(AppError.Timeout)
        assertNull(failure.getOrNull())
    }

    @Test
    fun `onSuccess runs on success and not on failure`() {
        var seen = 0
        Outcome.Success(5).onSuccess { seen = it }.onFailure { seen = -1 }
        assertEquals(5, seen)
    }

    @Test
    fun `onFailure runs on failure and not on success`() {
        var called = false
        Outcome.Failure(AppError.Network).onFailure { called = true }.onSuccess { }
        assertEquals(true, called)
    }

    @Test
    fun `asSuccess wraps value`() {
        assertEquals(Outcome.Success("hello"), "hello".asSuccess())
    }

    @Test
    fun `asFailure wraps error`() {
        assertEquals(Outcome.Failure(AppError.NotFound), AppError.NotFound.asFailure())
    }
}
