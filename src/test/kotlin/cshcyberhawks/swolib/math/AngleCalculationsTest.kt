package cshcyberhawks.swolib.math

import org.junit.Test

import org.junit.Assert.*

class AngleCalculationsTest {

    @Test
    fun wrapAroundAngles() {
        assertEquals(0.0, AngleCalculations.wrapAroundAngles(0.0), 0.0)
        assertEquals(180.0, AngleCalculations.wrapAroundAngles(180.0), 0.0)
        assertEquals(360.0, AngleCalculations.wrapAroundAngles(360.0), 0.0)
        assertEquals(180.0, AngleCalculations.wrapAroundAngles(-180.0), 0.0)
        assertEquals(0.0, AngleCalculations.wrapAroundAngles(-360.0), 0.0)
    }

    @Test
    fun optimizeAngle() {
        assertEquals(0.0, AngleCalculations.optimizeAngle(180.0, 0.0), 0.0)
        assertEquals(180.0, AngleCalculations.optimizeAngle(0.0, 180.0), 0.0)
        assertEquals(0.0, AngleCalculations.optimizeAngle(0.0, 0.0), 0.0)
        assertEquals(90.0, AngleCalculations.optimizeAngle(90.0, 0.0), 0.0)
        assertEquals(270.0, AngleCalculations.optimizeAngle(90.0, 270.0), 0.0)
    }
}