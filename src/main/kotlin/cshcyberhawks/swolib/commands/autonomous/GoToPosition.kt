package cshcyberhawks.swolib.commands.autonomous

import cshcyberhawks.swolib.autonomous.SwerveAuto
import cshcyberhawks.swolib.math.FieldPosition
import cshcyberhawks.swolib.math.Vector2
import edu.wpi.first.wpilibj2.command.CommandBase

class GoToPosition(val swerveAuto: SwerveAuto, val desiredPosition: FieldPosition) : CommandBase() {
    init {
        addRequirements(swerveAuto.swerveSystem)
    }

    constructor (swerveAuto: SwerveAuto, pos: Vector2) : this(swerveAuto, FieldPosition(pos.x, pos.y, 0.0))

    override fun initialize() {
        swerveAuto.desiredPosition = desiredPosition
    }

    override fun execute() {
        swerveAuto.move()
    }

    override fun isFinished(): Boolean {
        return swerveAuto.isFinishedMoving()
    }
}
