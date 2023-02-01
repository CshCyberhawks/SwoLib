package cshcyberhawks.swolib.math

import kotlin.math.atan2
import kotlin.math.sqrt

class Polar(var theta: Double = 0.0, var r: Double = 0.0) {
    companion object {
        fun fromVector2(cord: Vector2): Polar =
            Polar(Math.toDegrees(atan2(cord.y, cord.x)), sqrt(cord.x * cord.x + cord.y * cord.y))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Polar

        return (r == other.r && theta == other.theta) || (theta == (other.theta + 180) % 360 && r == -other.r)
    }
}