package cshcyberhawks.swolib.autonomous.commands

import cshcyberhawks.swolib.autonomous.SwerveAuto
import cshcyberhawks.swolib.math.FieldPosition
import cshcyberhawks.swolib.math.Vector2
import edu.wpi.first.wpilibj2.command.CommandBase

class GoToPositionAndExecute(
    val swerveAuto: SwerveAuto,
    val desiredPosition: FieldPosition,
    val command: CommandBase
) : CommandBase() {
    init {
        addRequirements(swerveAuto.swerveSystem)
    }

    enum class FinishCondition {
        POSITION,
        COMMAND,
        BOTH,
        EITHER
    }

    private var finishCondition = FinishCondition.BOTH

    // TODO: implement the turning to face the object
    constructor(
        swerveAuto: SwerveAuto,
        pos: Vector2,
        command: CommandBase
    ) : this(swerveAuto, FieldPosition(pos.x, pos.y, 0.0), command)

    constructor(
        swerveAuto: SwerveAuto,
        pos: Vector2,
        command: CommandBase,
        finishCondition: FinishCondition
    ) : this(swerveAuto, FieldPosition(pos.x, pos.y, 0.0), command) {
        this.finishCondition = finishCondition
    }

    override fun initialize() {
        swerveAuto.desiredPosition = desiredPosition
        command.schedule()
    }

    override fun execute() {
        swerveAuto.move()
    }

    override fun isFinished(): Boolean {
        when (finishCondition) {
            FinishCondition.POSITION -> return swerveAuto.isFinishedMoving()
            FinishCondition.COMMAND -> return command.isFinished
            FinishCondition.BOTH -> return swerveAuto.isFinishedMoving() && command.isFinished
            FinishCondition.EITHER -> return swerveAuto.isFinishedMoving() || command.isFinished
        }
    }
}
