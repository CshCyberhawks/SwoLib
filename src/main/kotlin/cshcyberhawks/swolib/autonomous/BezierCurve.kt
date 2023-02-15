package cshcyberhawks.swolib.autonomous

import cshcyberhawks.swolib.math.Vector2
import kotlin.math.pow
import kotlin.math.sqrt

class BezierCurve(private val startPoint: Vector2, private val controlPoint1: Vector2, private val controlPoint2: Vector2, private val endPoint: Vector2, private val resolution: Double) {
    private fun getPoint(time: Double): Vector2 {
        // https://en.wikipedia.org/wiki/B%C3%A9zier_curve#Cubic_B%C3%A9zier_curves
        // This is just the wikipedia Cubic Bezier with some operations unrolled to make it run faster
        return Vector2(
            (1 - time) * (1 - time) * (1 - time) * startPoint.x +
                    3 * (1 - time) * (1 - time) * time * controlPoint1.x +
                    3 * (1 - time) * time * time * controlPoint2.x +
                    time * time * time * endPoint.x,
            (1 - time) * (1 - time) * (1 - time) * startPoint.y +
                    3 * (1 - time) * (1 - time) * time * controlPoint1.y +
                    3 * (1 - time) * time * time * controlPoint2.y +
                    time * time * time * endPoint.y
        )
    }

    private val points: List<Vector2> = (0..resolution.toInt()).map { getPoint(it / resolution) }

    private fun getTotalDistance(): Double {
        var dist = 0.0

        for (i in 1 until points.size) {
            val p1 = points[i - 1]
            val p2 = points[i]

            dist += sqrt((p1.x - p2.x) * (p1.x - p2.x) - (p1.y - p2.y) * (p1.y - p2.y))
        }

        return dist
    }
}