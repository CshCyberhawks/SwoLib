package cshcyberhawks.swolib.math

import kotlin.math.abs

/**
 * A collection of methods for calculations related to angles.
 */
object AngleCalculations {
    /**
     * A function that changes an angle range from -180-180 to 0-360.
     */
    fun wrapAroundAngles(angle: Number): Number {
        var angle: Double = angle.toDouble()
        while (angle < 0) {
            angle += 360
        }
        return angle
    }

    /**
     * A function that calculates the which way a wheel should be facing for the least rotation.
     */
    fun optimizeAngle(desiredAngle: Number, currentAngle: Number): Number {
        val desiredAngle: Double = desiredAngle.toDouble()
        val currentAngle: Double = currentAngle.toDouble()
        if (abs(desiredAngle - currentAngle) > 90 && abs(desiredAngle - currentAngle) < 270) {
            return (desiredAngle + 180) % 360
        }
        return desiredAngle
    }
}