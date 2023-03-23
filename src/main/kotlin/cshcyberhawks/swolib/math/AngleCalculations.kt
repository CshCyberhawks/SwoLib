package cshcyberhawks.swolib.math

/**
 * A collection of methods for calculations related to angles.
 */
object AngleCalculations {
    /**
     * A function that changes an angle range from -180-180 to 0-360.
     *
     * @param inputAngle The angle you want to convert.
     *
     * @return The converted angle.
     */
    fun wrapAroundAngles(inputAngle: Double): Double {
        val angle = inputAngle % 360
        return if (angle < 0) {
            angle + 360
        } else angle
    }

    /**
     * A function that calculates the which way a wheel should be facing for the least rotation.
     *
     * @param desiredAngle The angle you want to face towards.
     * @param currentAngle The angle you are currently at.
     *
     * @return The optimized angle.
     */
    fun optimizeAngle(desiredAngle: Double, currentAngle: Double): Double {
        val diff = wrapAroundAngles(desiredAngle - currentAngle)
        return if (diff > 90 && diff < 270) {
            (desiredAngle + 180) % 360
        } else desiredAngle
    }
}