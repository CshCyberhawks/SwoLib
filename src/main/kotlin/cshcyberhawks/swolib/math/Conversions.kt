package cshcyberhawks.swolib.math

import kotlin.math.PI

/**
 * A collection of methods for conversions
 */
object Conversions {
    /**
     * A function converting a quantity of rotations to a distance in meters with respect to gear ratio
     * @param rotations The number of rotations to be converted
     * @param radius The radius of the wheel
     * @param gearRatio The net gear ratio from the motor to the wheel
     *
     * @return A distance in meters traveled
     */
    fun rotationsToMeters(rotations: Double, radius: Double, gearRatio: Double): Double =
        rotations * 2 * PI * radius / gearRatio

    /**
     * A function converting a quantity of rotations to a distance in meters assuming a gear ratio of one
     *
     * @param rotations The number of rotations to be converted
     * @param radius The radius of the wheel
     *
     * @return A distance in meters traveled
     */
    fun rotationsToMeters(rotations: Double, radius: Double): Double = rotationsToMeters(rotations, radius, 1.0)
}