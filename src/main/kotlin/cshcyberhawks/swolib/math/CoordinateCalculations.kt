package cshcyberhawks.swolib.math

import kotlin.math.*

object CoordinateCalculations {
    fun cartesianToPolar(coordinate: Vector2): Polar {
        val r = sqrt(coordinate.x.pow(2) + coordinate.y.pow(2))
        val theta = Math.toDegrees(atan2(coordinate.y, coordinate.x))
        return Polar(theta, r)
    }

    fun cartesianToPolar(coordinateX: Number, coordinateY: Number): Polar {
        return cartesianToPolar(Vector2(coordinateX, coordinateY))
    }

    fun polarToCartesian(coordinate: Polar): Vector2 {
        val theta = Math.toRadians(coordinate.theta)
        val x = coordinate.r * cos(theta)
        val y = coordinate.r * sin(theta)
        return Vector2(x, y)
    }

    fun polarToCartesian(coordinateTheta: Number, coordinateR: Number): Vector2 {
        return polarToCartesian(Polar(coordinateTheta, coordinateR))
    }
}