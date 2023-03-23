package cshcyberhawks.swolib.autonomous.commands

import cshcyberhawks.swolib.autonomous.SwerveAuto
import cshcyberhawks.swolib.math.FieldPosition
import cshcyberhawks.swolib.math.Vector2
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.CommandBase

class GoToPosition(
        val swerveAuto: SwerveAuto,
        private val desiredPosition: FieldPosition,
        val desiredVelocity: Vector2 = Vector2(0.0, 0.0)
) : CommandBase() {
    constructor(
            swerveAuto: SwerveAuto,
            pos: Vector2,
            desiredEndVelocity: Vector2 = Vector2(0.0, 0.0)
    ) : this(swerveAuto, FieldPosition(pos.x, pos.y, 0.0), desiredEndVelocity)

    override fun initialize() {
        swerveAuto.desiredPosition = desiredPosition
        swerveAuto.setDesiredEndVelocity(desiredVelocity)
    }

    override fun execute() {
        swerveAuto.move()
    }

    override fun isFinished(): Boolean {
        SmartDashboard.putBoolean("Go To Pos Finished", swerveAuto.isFinishedMoving())
        return swerveAuto.isFinishedMoving()
    }

    override fun end(int: Boolean) {
        swerveAuto.kill()
    }
}
