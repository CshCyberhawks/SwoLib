package cshcyberhawks.swolib.math

import kotlin.math.PI

/***
 * A collection of methods for converting between units of measure
 */
object Conversions {
    /***
     * A function that converts from rotations per second to meters per second
     *
     * @param rotations The input speed in rotations per second
     * @param radius The radius of the wheel in meters
     * @param gearRatio The gear ratio of the wheel
     *
     * @return The speed in meters per second
     */
    fun rotationsPerSecondToMetersPerSecond(rotations: Double, radius: Double, gearRatio: Double): Double =
        rotations * 2 * PI * radius / gearRatio

    /***
     * A function that converts from rotations per second to meters per second
     *
     * @param rotations The input speed in rotations per second
     * @param radius The radius of the wheel in meters
     *
     * @return The speed in meters per second
     */
    fun rotationsPerSecondToMetersPerSecond(rotations: Double, radius: Double): Double = rotationsPerSecondToMetersPerSecond(rotations, radius, 1.0)
}