package cshcyberhawks.swolib.math

import java.lang.Math.toDegrees
import java.lang.Math.toRadians
import kotlin.math.*

class Coordinate(var x: Double = 0.0, var y: Double = 0.0) {
    var theta: Double
        get() = toDegrees(atan2(y, x))
        set(value) {
            val len = r
            x = len * cos(toRadians(value))
            y = len * sin(toRadians(value))
        }
    var r: Double
        get() = sqrt(x.pow(2) + y.pow(2))
        set(value) {
            val t = theta
            x = value * cos(toRadians(t))
            y = value * sin(toRadians(t))
        }

    companion object {
        fun fromPolar(theta: Double, r: Double): Coordinate = Coordinate(r * sin(theta), r * cos(theta))
    }

    operator fun plus(other: Coordinate): Coordinate = Coordinate(x + other.x, y + other.y)

    operator fun minus(other: Coordinate): Coordinate = Coordinate(x - other.x, y - other.y)

    operator fun timesAssign(other: Double) {
        x *= other
        y *= other
    }

    operator fun divAssign(other: Int) {
        x /= other
        y /= other
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Coordinate

        return (x == other.x) && (y == other.y)
    }
}