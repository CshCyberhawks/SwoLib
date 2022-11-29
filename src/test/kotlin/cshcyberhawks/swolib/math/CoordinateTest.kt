package cshcyberhawks.swolib.math

import org.junit.Test

import org.junit.Assert.*
import kotlin.math.sqrt

class CoordinateTest {
    @Test
    fun getTheta() {
        assertEquals(0.0, Coordinate(0.0, 0.0).theta, 0.0)
        assertEquals(0.0, Coordinate(1.0, 0.0).theta, 0.0)
        assertEquals(90.0, Coordinate(0.0, 1.0).theta, 0.0)
        assertEquals(180.0, Coordinate(-1.0, 0.0).theta, 0.0)
        assertEquals(-90.0, Coordinate(0.0, -1.0).theta, 0.0)
        assertEquals(45.0, Coordinate(1.0, 1.0).theta, 0.0)
        assertEquals(135.0, Coordinate(-1.0, 1.0).theta, 0.0)
        assertEquals(-135.0, Coordinate(-1.0, -1.0).theta, 0.0)
        assertEquals(-45.0, Coordinate(1.0, -1.0).theta, 0.0)
    }

    @Test
    fun setTheta() {
        assertEquals(0.0, Coordinate(1.0, 1.0).apply { theta = 0.0 }.theta, 1.0)
        assertEquals(45.0, Coordinate(1.0, 1.0).apply { theta = 45.0 }.theta, 1.0)
        assertEquals(90.0, Coordinate(1.0, 1.0).apply { theta = 90.0 }.theta, 1.0)
        assertEquals(180.0, Coordinate(1.0, 1.0).apply { theta = 180.0 }.theta, 1.0)
        assertEquals(-90.0, Coordinate(1.0, 1.0).apply { theta = 270.0 }.theta, 1.0)
        assertEquals(0.0, Coordinate(1.0, 1.0).apply { theta = 360.0 }.theta, 1.0)
    }

    @Test
    fun getR() {
        assertEquals(0.0, Coordinate(0.0, 0.0).r, 0.0)
        assertEquals(1.0, Coordinate(0.0, 1.0).r, 0.0)
        assertEquals(1.0, Coordinate(1.0, 0.0).r, 0.0)
        assertEquals(sqrt(2.0), Coordinate(1.0, 1.0).r, 0.0)
        assertEquals(1.0, Coordinate(0.0, -1.0).r, 0.0)
    }

    @Test
    fun setR() {
        assertEquals(0.0, Coordinate().apply { r = 0.0 }.r, 0.0)
        assertEquals(1.0, Coordinate().apply { r = 1.0 }.r, 0.0)
        assertEquals(2.0, Coordinate().apply { r = 2.0 }.r, 0.0)
        assertEquals(3.0, Coordinate().apply { r = 3.0 }.r, 0.0)
        assertEquals(4.0, Coordinate().apply { r = 4.0 }.r, 0.0)
    }

    @Test
    fun plus() {
        assert(Coordinate(1.0, 3.0) + Coordinate(2.0, 2.0) == Coordinate(3.0, 5.0))
        assert(Coordinate(3.0, 1.0) + Coordinate(2.0, 3.0) == Coordinate(5.0, 4.0))
        assert(Coordinate(4.0, 5.0) + Coordinate(8.0, 3.0) == Coordinate(12.0, 8.0))
    }

    @Test
    fun minus() {
        assert(Coordinate(1.0, 3.0) - Coordinate(2.0, 2.0) == Coordinate(-1.0, 1.0))
        assert(Coordinate(3.0, 1.0) - Coordinate(2.0, 3.0) == Coordinate(1.0, -2.0))
        assert(Coordinate(4.0, 5.0) - Coordinate(8.0, 3.0) == Coordinate(-4.0, 2.0))
    }

    @Test
    fun testEquals() {
        assert(Coordinate(1.0, 3.0) == Coordinate(1.0, 3.0))
        assert(Coordinate(5.0, 6.0) == Coordinate(5.0, 6.0))
        assert(Coordinate(9.0, 3.0) == Coordinate(9.0, 3.0))
    }
}