package cshcyberhawks.swolib.math

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class PolarTest {
    @Test
    fun testEquals() {
        assert(Polar(90.0, 1.0) == Polar(90.0, 1.0))
        assert(Polar(90.0, 1.0) == Polar(270.0, -1.0))
    }
}