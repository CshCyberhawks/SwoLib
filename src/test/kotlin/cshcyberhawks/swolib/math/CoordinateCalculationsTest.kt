package cshcyberhawks.swolib.math

import org.junit.Test
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

internal class CoordinateCalculationsTest {
    @Test
    fun cartesianToPolarTest() {
        val a = Vector2(3, 4)
        val b = CoordinateCalculations.cartesianToPolar(a)

        assert(b == Polar(Math.toDegrees(atan2(4.0, 3.0)), 5))
    }

    @Test
    fun polarToCartesianTest() {
        val a = Polar(100, 4)
        val b = CoordinateCalculations.polarToCartesian(a)

        assert(b == Vector2(4 * cos(Math.toRadians(100.0)), 4 * sin(Math.toRadians(100.0))))
    }
}