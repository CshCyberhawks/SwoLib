package cshcyberhawks.swolib.swerve

import cshcyberhawks.swolib.math.Coordinate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.math.sqrt


class SwerveDriveTrainTest {
    @Test
    fun normalizeWheelSpeeds() {
        //Test 1: All speeds are positive
        val wheelSpeeds1 = arrayOf(0.25, 0.5, 0.75, 1.0)
        val wheelVectors1 = arrayOf(1.0, 2.0, 3.0, 4.0)
        val normalizedWheelVectors1 = SwerveDriveTrain.normalizeWheelSpeeds(wheelVectors1, 1.0)
        for (i in wheelSpeeds1.indices) {
            assertEquals(wheelSpeeds1[i], normalizedWheelVectors1[i], 0.0)
        }

        //Test 2: All speeds are negative
        val wheelSpeeds2 = arrayOf(-0.25, -0.5, -0.75, -1.0)
        val wheelVectors2 = arrayOf(-1.0, -2.0, -3.0, -4.0)
        val normalizedWheelVectors2 = SwerveDriveTrain.normalizeWheelSpeeds(wheelVectors2, 1.0)
        for (i in wheelSpeeds2.indices) {
            assertEquals(wheelSpeeds2[i], normalizedWheelVectors2[i], 0.0)
        }

        //Test 3: All speeds are mixed
        val wheelSpeeds3 = arrayOf(0.25, -0.5, 0.75, -1.0)
        val wheelVectors3 = arrayOf(1.0, -2.0, 3.0, -4.0)
        val normalizedWheelVectors3 = SwerveDriveTrain.normalizeWheelSpeeds(wheelVectors3, 1.0)
        for (i in wheelSpeeds3.indices) {
            assertEquals(wheelSpeeds3[i], normalizedWheelVectors3[i], 0.0)
        }

        //Test 4: All speeds are below the distance
        val wheelSpeeds4 = arrayOf(0.25, 0.5, 0.75)
        val wheelVectors4 = arrayOf(0.25, 0.5, 0.75)
        val normalizedWheelVectors4 = SwerveDriveTrain.normalizeWheelSpeeds(wheelVectors4, 1.0)
        for (i in wheelSpeeds4.indices) {
            assertEquals(wheelSpeeds4[i], normalizedWheelVectors4[i], 0.0)
        }
    }

    private fun assertCoordinate(cord1: Coordinate, cord2: Coordinate) {
        assertEquals(cord1.x, cord2.x, 0.01)
        assertEquals(cord1.y, cord2.y, 0.01)
    }

    @Test
    fun calculateDrive() {
        assertCoordinate(Coordinate(1.0, 1.0), SwerveDriveTrain.calculateDrive(Coordinate(1.0, 1.0), Coordinate(), 0.0))
        assertCoordinate(Coordinate.fromPolar(45.0, 1.0), SwerveDriveTrain.calculateDrive(Coordinate(0.0, 0.0), Coordinate.fromPolar(45.0, 1.0), 0.0))
        assertCoordinate(Coordinate(0.0, 1.0), SwerveDriveTrain.calculateDrive(Coordinate(1.0, 0.0), Coordinate(), 90.0))
        assertCoordinate(Coordinate(1.0 + 1.0 / sqrt(2.0), 1.0 / sqrt(2.0)), SwerveDriveTrain.calculateDrive(Coordinate(1.0, 0.0), Coordinate.fromPolar(45.0, 1.0), 0.0))
    }
}