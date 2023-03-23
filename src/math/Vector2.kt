package cshcyberhawks.swolib.math

import kotlin.math.cos
import kotlin.math.sin

/**
 * A class to represent a cartesian vector
 *
 * @param x The value on the x-axis
 * @param y The value on the y-axis
 */
class Vector2(var x: Double = 0.0, var y: Double = 0.0) {
    companion object {
        /**
         * A function to construct a vector2 2 from a polar vector
         *
         * @param cord The polar vector input
         *
         * @return The equivalent polar vector
         */
        fun fromPolar(cord: Polar): Vector2 =
            Vector2(cord.r * cos(Math.toRadians(cord.theta)), cord.r * sin(Math.toRadians(cord.theta)))
    }

    operator fun plus(other: Vector2): Vector2 = Vector2(x + other.x, y + other.y)

    operator fun div(other: Int): Vector2 = Vector2(x / other, y / other)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Vector2

        return x == other.x && y == other.y
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }
}