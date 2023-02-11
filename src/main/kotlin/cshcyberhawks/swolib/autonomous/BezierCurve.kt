package cshcyberhawks.swolib.autonomous

import cshcyberhawks.swolib.math.Vector2
import kotlin.math.sqrt

class BezierCurve(private val startPoint: Vector2, private val modPoint: Vector2, private val endPoint: Vector2, private val resolution: Double) {
    private fun getChange(num1: Double, num2: Double, time: Double): Double = num1 + (num2 - num1) * time

    private fun getPoint(time: Double): Vector2 {
        val tanXA = getChange(startPoint.x, modPoint.x, time)
        val tanXB = getChange(modPoint.x, endPoint.x, time)
        val tanYA = getChange(startPoint.y, modPoint.y, time)
        val tanYB = getChange(modPoint.y, endPoint.y, time)

        return Vector2(
            getChange(tanXA, tanXB, time),
            getChange(tanYA, tanYB, time)
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