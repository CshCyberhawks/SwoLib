package cshcyberhawks.swolib.math

import java.lang.Math.toDegrees
import java.lang.Math.toRadians
import kotlin.math.*

/**
 * A class representing a coordinate point which has values and functions for polar and cartesian coordinates as well as for performing arithmetic and comparative operations with others of the same class
 *
 * @property x The x-coordinate of the point
 * @property y The y-coordinate of the point
 * @property theta The theta value of the point
 * @property r The radius value of the point
 */
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

    /**
     * Has a function to convert polar coordinates to a coordinate class
     */
    companion object {
        /**
         * Converts polar coordinates to a coordinate class
         *
         * @param theta The theta value of the polar coordinates
         * @param r The radius value of the polar coordinates
         *
         * @return A coordinate class with cartesian coordinates
         */
        fun fromPolar(theta: Double, r: Double): Coordinate = Coordinate(r * sin(theta), r * cos(theta))
    }

    /**
     * Adds coordinates together
     *
     * @param other Another coordinate which is being added
     *
     * @return A coordinate with coordinates being the sum of the original coordinates' coordinates
     */
    operator fun plus(other: Coordinate): Coordinate = Coordinate(x + other.x, y + other.y)

    /**
     * Subtracts coordinates
     *
     * @param other Another coordinate which is being subtracted
     *
     * @return A coordinate with coordinates being the difference of the self coordinate and the other coordinate
     */
    operator fun minus(other: Coordinate): Coordinate = Coordinate(x - other.x, y - other.y)

    /**
     * Multiplies the coordinates by a given value
     *
     * @param other The number which the coordinates are multiplied by
     *
     * @return It doesn't, but multiplies the coordinates of the original coordinate by the given number
     */
    operator fun timesAssign(other: Double) {
        x *= other
        y *= other
    }

    /**
     * Compares self coordinate with another thing to test equality
     *
     * @param other The other value which is being compared to the coordinate
     *
     * @returns True if they are equal, and false if they aren't
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Coordinate

        return (x == other.x) && (y == other.y)
    }
}