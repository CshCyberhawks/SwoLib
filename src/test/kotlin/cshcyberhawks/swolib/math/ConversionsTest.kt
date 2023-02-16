package cshcyberhawks.swolib.math

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class ConversionsTest {

    @Test
    fun rotationsToMeters() {
        assertEquals(Conversions.rotationsPerSecondToMetersPerSecond(5.0, 0.1), 3.14, 0.1)
        assertEquals(Conversions.rotationsPerSecondToMetersPerSecond(1.5, 0.25, 7.0), 0.34, 0.1)
        assertEquals(Conversions.rotationsPerSecondToMetersPerSecond(2.0, 0.5), 6.28, 0.1)
    }
}