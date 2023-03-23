package cshcyberhawks.swolib.math

import cshcyberhawks.swolib.swerve.SwerveOdometry
import edu.wpi.first.math.trajectory.TrapezoidProfile
import edu.wpi.first.util.WPIUtilJNI
import kotlin.math.sqrt

class BezierCurve(
    swo: SwerveOdometry,
    private val trapConstraints: TrapezoidProfile.Constraints,
    private val startPoint: Vector2,
    private val controlPoint1: Vector2,
    private val controlPoint2: Vector2,
    private val endPoint: Vector2,
    private val resolution: Double
) {
    private fun getPoint(time: Double): Vector2 {
        // https://en.wikipedia.org/wiki/Bézier_curve
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
