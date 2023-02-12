package cshcyberhawks.swolib.autonomous

import cshcyberhawks.swolib.math.Vector2
import cshcyberhawks.swolib.swerve.SwerveOdometry
import edu.wpi.first.math.trajectory.TrapezoidProfile
import kotlin.math.sqrt

class BezierCurve(private val swo: SwerveOdometry, private val startPoint: Vector2, private val modPoint: Vector2, private val endPoint: Vector2, private val resolution: Double) {
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

    val points: List<Vector2> = (0..resolution.toInt()).map { getPoint(it / resolution) }
    var nextPoint: Int = 1

    private var trapYCurrentState: TrapezoidProfile.State =
        TrapezoidProfile.State(
            swo.fieldPosition.y, swo.getVelocity().y
        )
    private var trapYDesiredState: TrapezoidProfile.State =
        TrapezoidProfile.State(points[points.size - 1].y, 0.0)

    private var trapXCurrentState: TrapezoidProfile.State =
        TrapezoidProfile.State(
            swo.fieldPosition.x, swo.getVelocity().x
        )
    private var trapXDesiredState: TrapezoidProfile.State =
        TrapezoidProfile.State(points[points.size - 1].x, 0.0)

    fun getNextPoint(): Vector2 {
        trapYCurrentState = TrapezoidProfile.State

        return points[nextPoint++]
    }

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