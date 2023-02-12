package cshcyberhawks.swolib.autonomous.commands

import cshcyberhawks.swolib.autonomous.BezierCurve
import cshcyberhawks.swolib.autonomous.SwerveAuto
import cshcyberhawks.swolib.math.FieldPosition
import edu.wpi.first.wpilibj2.command.CommandBase

class GoToPositionBezier(private val swerveAuto: SwerveAuto, private val curve: BezierCurve): CommandBase() {
    override fun initialize() {
        swerveAuto.desiredPosition = FieldPosition(curve.getNextPoint(), 0.0)
    }

    override fun execute() {
        if (swerveAuto.isFinishedMoving() && curve.nextPoint != curve.points.size) {
            swerveAuto.desiredPosition = FieldPosition(curve.getNextPoint(), 0.0)
        }
        swerveAuto.move()
    }

    override fun isFinished(): Boolean {
        return swerveAuto.isFinishedMoving() && curve.nextPoint == curve.points.size
    }
}