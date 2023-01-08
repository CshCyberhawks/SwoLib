package cshcyberhawks.swolib.math

import kotlin.math.PI

object Conversions {
    fun rotationsToMeters(rotations: Double, radius: Double, gearRatio: Double): Double = rotations * 2 * PI * radius / gearRatio
    fun rotationsToMeters(rotations: Double, radius: Double): Double = rotationsToMeters(rotations, radius, 1.0)
}