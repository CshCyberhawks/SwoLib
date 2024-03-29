package cshcyberhawks.swolib.math

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class MiscCalculationsTest {

    @Test
    fun gToMetersPerSecond() {
        assertEquals(0.0, MiscCalculations.gToMetersPerSecond(0.0), 0.0)
        assertEquals(9.8066, MiscCalculations.gToMetersPerSecond(1.0), 0.0)
        assertEquals(19.6132, MiscCalculations.gToMetersPerSecond(2.0), 0.0)
        assertEquals(29.4198, MiscCalculations.gToMetersPerSecond(3.0), 0.0)
        assertEquals(39.2264, MiscCalculations.gToMetersPerSecond(4.0), 0.0)
    }

    @Test
    fun calculateDeadzone() {
        assertEquals(0.0, MiscCalculations.calculateDeadzone(1.2, 2.0), 0.0)
        assertEquals(.1, MiscCalculations.calculateDeadzone(2.2, 2.0), 0.000001)
        assertEquals(0.0, MiscCalculations.calculateDeadzone(-1.2, 2.0), 0.0)
        assertEquals(-.1, MiscCalculations.calculateDeadzone(-2.2, 2.0), 0.000001)
    }

    @Test
    fun closestPoint() {
        val vecArr = arrayOf(Vector2(0.0, 0.0), Vector2(1.0, 1.0), Vector2(2.0, 2.0), Vector2(1.5, 3.0), Vector2(1.0, 5.0))

        assertEquals(Vector2(0.0, 0.0), MiscCalculations.closestPoint(Vector2(0.0, 0.0), vecArr))

        assertEquals(Vector2(0.0, 0.0), MiscCalculations.closestPoint(Vector2(1.0, 0.0), vecArr))
        assertEquals(Vector2(1.5, 3.0), MiscCalculations.closestPoint(Vector2(2.0, 3.0), vecArr))
        assertEquals(Vector2(1.0, 5.0), MiscCalculations.closestPoint(Vector2(2.0, 4.2), vecArr))
    }
}
