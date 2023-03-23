package cshcyberhawks.swolib.autonomous.commands

import cshcyberhawks.swolib.autonomous.SwerveAuto
import cshcyberhawks.swolib.math.BezierCurve
import cshcyberhawks.swolib.math.FieldPosition
import edu.wpi.first.wpilibj2.command.CommandBase

class GoToPositionBezier(private val swerveAuto: SwerveAuto, private val curve: BezierCurve) :
    CommandBase() {
    override fun initialize() {
        val (point, velo) = curve.getNextPoint()
        swerveAuto.desiredPosition = FieldPosition(point, 0.0)
        swerveAuto.setDesiredEndVelocity(velo)
    }

    override fun execute() {
        if (swerveAuto.isFinishedMoving() && curve.nextPoint != curve.points.size) {
            val (point, velo) = curve.getNextPoint()
            swerveAuto.desiredPosition = FieldPosition(point, 0.0)
            swerveAuto.setDesiredEndVelocity(velo)
        }
        swerveAuto.move()
    }

    override fun isFinished(): Boolean {
        return swerveAuto.isFinishedMoving() && curve.nextPoint == curve.points.size
    }

    override fun end(int: Boolean) {
        swerveAuto.kill()
    }
}
