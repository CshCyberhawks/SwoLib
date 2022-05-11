package cshcyberhawks.swolib.math

import kotlin.math.abs

object AngleCalculations {
    fun wrapAroundAngles(angle: Number): Number {
        var angle: Double = angle.toDouble()
        while (angle < 0) {
            angle += 360
        }
        return angle
    }

    fun optimizeAngle(desiredAngle: Number, currentAngle: Number): Number {
        val desiredAngle: Double = desiredAngle.toDouble()
        val currentAngle: Double = currentAngle.toDouble()
        if (abs(desiredAngle - currentAngle) > 90 && abs(desiredAngle - currentAngle) < 270) {
            return (desiredAngle + 180) % 360
        }
        return desiredAngle
    }
}