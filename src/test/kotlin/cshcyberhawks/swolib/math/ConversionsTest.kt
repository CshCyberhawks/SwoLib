package cshcyberhawks.swolib.math

import org.junit.Test

import org.junit.Assert.*

class ConversionsTest {

    @Test
    fun rotationsToMeters() {
        assertEquals(Conversions.rotationsToMeters(5.0, 0.1), 3.14, 0.1)
        assertEquals(Conversions.rotationsToMeters(1.5, 0.25, 7.0), 0.34, 0.1)
        assertEquals(Conversions.rotationsToMeters(2.0, 0.5), 6.28, 0.1)
    }
}