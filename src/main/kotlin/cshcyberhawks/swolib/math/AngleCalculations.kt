package cshcyberhawks.swolib.math

object AngleCalculations {
    fun wrapAroundAngles(angle: Number): Number {
        var angle: Double = angle.toDouble()
        while (angle < 0) {
            angle += 360
        }
        return angle
    }
}