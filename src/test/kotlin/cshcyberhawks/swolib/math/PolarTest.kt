package cshcyberhawks.swolib.math

import org.junit.Test

internal class PolarTest {
    @Test
    fun testEquals() {
        val a = Polar(342, 3)
        val b = Polar(342, 3)

        assert(a == b)
    }
}