package cshcyberhawks.swolib.math

import kotlin.math.*

/**
 * A collection of methods for calculations related to coordinate systems.
 */
object CoordinateCalculations {
    /**
     * Converts from a cartesian coordinate (x, y) to a polar coordinate (theta, r).
     *
     * @param coordinate A vector2 representing the coordinate you want to convert.
     *
     * @return A polar coordinate representing the converted coordinate.
     */
    fun cartesianToPolar(coordinate: Vector2): Polar {
        val r = sqrt(coordinate.x.pow(2) + coordinate.y.pow(2))
        val theta = Math.toDegrees(atan2(coordinate.y, coordinate.x))
        return Polar(theta, r)
    }

    /**
     * Converts from a cartesian coordinate (x, y) to a polar coordinate (theta, r).
     *
     * @param coordinateX The x value for the coordinate you want to convert.
     * @param coordinateY The y value for the coordinate you want to convert.
     *
     * @return A polar coordinate representing the converted coordinate.
     */
    fun cartesianToPolar(coordinateX: Number, coordinateY: Number): Polar {
        return cartesianToPolar(Vector2(coordinateX, coordinateY))
    }

    /**
     * Converts from a polar coordinate (theta, r) to a cartesian coordinate (x, y).
     *
     * @param coordinate The polar coordinate that you want to convert.
     *
     * @return A vector2 representing the converted coordinate.
     */
    fun polarToCartesian(coordinate: Polar): Vector2 {
        val theta = Math.toRadians(coordinate.theta)
        val x = coordinate.r * cos(theta)
        val y = coordinate.r * sin(theta)
        return Vector2(x, y)
    }

    /**
     * Converts from a polar coordinate (theta, r) to a cartesian coordinate (x, y).
     *
     * @param coordinateTheta The theta value for the coordinate you want to convert.
     * @param coordinateR The r value for the coordinate you want to convert.
     *
     * @return A vector2 representing the converted coordinate.
     */
    fun polarToCartesian(coordinateTheta: Number, coordinateR: Number): Vector2 {
        return polarToCartesian(Polar(coordinateTheta, coordinateR))
    }
}