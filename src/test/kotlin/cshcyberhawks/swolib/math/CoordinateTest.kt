package cshcyberhawks.swolib.math

import org.junit.Test

import org.junit.Assert.*

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

    }

    @Test
    fun getR() {
    }

    @Test
    fun setR() {
    }

    @Test
    fun plus() {
    }

    @Test
    fun minus() {
    }

    @Test
    fun testEquals() {
    }
}