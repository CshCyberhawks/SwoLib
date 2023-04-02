package cshcyberhawks.swolib.math

import kotlin.math.atan2
import kotlin.math.sqrt

/**
 * A class to represent a polar vector
 *
 * @param theta The angle of the vector
 * @param r The length of the vector
 */
class Polar(var theta: Double = 0.0, var r: Double = 0.0) {
    companion object {
        /**
         * A function that constructs a polar vector from a vector2
         *
         * @param cord The vector2 input
         *
         * @return The equivalent polar vector
         */
        fun fromVector2(cord: Vector2): Polar =
            Polar(Math.toDegrees(atan2(cord.y, cord.x)), sqrt(cord.x * cord.x + cord.y * cord.y))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Polar

        return (r == other.r && theta == other.theta) || (theta == (other.theta + 180) % 360 && r == -other.r)
    }


    override fun hashCode(): Int {
        var result = theta.hashCode()
        result = 31 * result + r.hashCode()
        return result
    }
}