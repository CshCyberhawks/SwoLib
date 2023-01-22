package cshcyberhawks.swolib.math

import kotlin.math.cos
import kotlin.math.sin

class Vector2(var x: Double = 0.0, var y: Double = 0.0) {
    companion object {
        fun fromPolar(cord: Polar): Vector2 = Vector2(cord.r * cos(Math.toRadians(cord.theta)), cord.r * sin(Math.toRadians(cord.theta)))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Vector2

        return x == other.x && y == other.y
    }
}