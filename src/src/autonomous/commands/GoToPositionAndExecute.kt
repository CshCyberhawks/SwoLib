package cshcyberhawks.swolib.autonomous.commands

import cshcyberhawks.swolib.autonomous.FinishCondition
import cshcyberhawks.swolib.autonomous.SwerveAuto
import cshcyberhawks.swolib.math.FieldPosition
import cshcyberhawks.swolib.math.Vector2
import edu.wpi.first.wpilibj2.command.CommandBase

class GoToPositionAndExecute(
    val swerveAuto: SwerveAuto,
    val desiredPosition: FieldPosition,
    val command: CommandBase,
    val finishCondition: FinishCondition = FinishCondition.BOTH
) : CommandBase() {
    // TODO: implement the turning to face the object
    constructor(
        swerveAuto: SwerveAuto,
        pos: Vector2,
        command: CommandBase,
        finishCondition: FinishCondition
    ) : this(swerveAuto, FieldPosition(pos.x, pos.y, 0.0), command, finishCondition)

    override fun initialize() {
        swerveAuto.desiredPosition = desiredPosition
        command.schedule()
    }

    override fun execute() {
        swerveAuto.move()
    }

    override fun end(interrupted: Boolean) {
        swerveAuto.kill();
        command.cancel()
    }
    override fun isFinished(): Boolean {
        return when (finishCondition) {
            FinishCondition.POSITION -> swerveAuto.isFinishedMoving()
            FinishCondition.COMMAND -> command.isFinished
            FinishCondition.BOTH -> swerveAuto.isFinishedMoving() && command.isFinished
            FinishCondition.EITHER -> swerveAuto.isFinishedMoving() || command.isFinished
        }
    }
}
