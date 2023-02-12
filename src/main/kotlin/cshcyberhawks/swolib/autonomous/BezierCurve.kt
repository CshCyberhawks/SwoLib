package cshcyberhawks.swolib.autonomous

import cshcyberhawks.swolib.math.Vector2
import cshcyberhawks.swolib.swerve.SwerveOdometry
import edu.wpi.first.math.trajectory.TrapezoidProfile
import edu.wpi.first.util.WPIUtilJNI
import kotlin.math.sqrt

class BezierCurve(
        private val swo: SwerveOdometry,
        private val trapConstraints: TrapezoidProfile.Constraints,
        private val startPoint: Vector2,
        private val modPoint: Vector2,
        private val endPoint: Vector2,
        private val resolution: Double
) {
    private fun getChange(num1: Double, num2: Double, time: Double): Double =
            num1 + (num2 - num1) * time

    private fun getPoint(time: Double): Vector2 {
        val tanXA = getChange(startPoint.x, modPoint.x, time)
        val tanXB = getChange(modPoint.x, endPoint.x, time)
        val tanYA = getChange(startPoint.y, modPoint.y, time)
        val tanYB = getChange(modPoint.y, endPoint.y, time)

        return Vector2(getChange(tanXA, tanXB, time), getChange(tanYA, tanYB, time))
    }

    val points: List<Vector2> = (0..resolution.toInt()).map { getPoint(it / resolution) }
    var nextPoint: Int = 1

    private var trapXCurrentState: TrapezoidProfile.State =
            TrapezoidProfile.State(swo.fieldPosition.x, swo.getVelocity().x)
    private var trapXDesiredState: TrapezoidProfile.State =
            TrapezoidProfile.State(getTotalDistance(), 0.0)
    private var trapYCurrentState: TrapezoidProfile.State =
            TrapezoidProfile.State(swo.fieldPosition.y, swo.getVelocity().y)
    private var trapYDesiredState: TrapezoidProfile.State =
            TrapezoidProfile.State(getTotalDistance(), 0.0)

    private var prevTime: Double = 0.0

    fun getNextPoint(): Pair<Vector2, Vector2> {
        val timeNow = WPIUtilJNI.now() * 1.0e-6
        val trapTime: Double = if (prevTime == 0.0) 0.0 else timeNow - prevTime

        val trapXProfile = TrapezoidProfile(trapConstraints, trapXDesiredState, trapXCurrentState)
        val trapYProfile = TrapezoidProfile(trapConstraints, trapYDesiredState, trapYCurrentState)

        val trapXOutput = trapXProfile.calculate(trapTime)
        val trapYOutput = trapYProfile.calculate(trapTime)


        trapXCurrentState = trapXOutput
        trapYCurrentState = trapYOutput
        prevTime = timeNow
        return Pair(points[nextPoint++], Vector2(trapXOutput.velocity, trapYOutput.velocity))
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
