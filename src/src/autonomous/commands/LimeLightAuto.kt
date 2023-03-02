package cshcyberhawks.swolib.commands.autonomous

import cshcyberhawks.swolib.autonomous.SwerveAuto
import cshcyberhawks.swolib.math.FieldPosition
import cshcyberhawks.swolib.math.Vector2
import edu.wpi.first.wpilibj2.command.CommandBase
import cshcyberhawks.swolib.limelight.Limelight

class LimeLightAuto(val swerveAuto: SwerveAuto, val limelight: Limelight, val targetHeight: Double) : CommandBase() {
    init {
        addRequirements(swerveAuto.swerveSystem)
    }

    override fun initialize() {
        swerveAuto.desiredPosition = FieldPosition(limelight.getPosition(swerveAuto.swo, targetHeight, swerveAuto.gyro), 0.0)
        swerveAuto.setDesiredAngleRelative(limelight.getHorizontalOffset())
    }

    override fun execute() {
        swerveAuto.move()
    }

    override fun isFinished(): Boolean {
        return swerveAuto.isFinishedMoving()
    }

    override fun end(int: Boolean) {
        swerveAuto.kill();
    }
}
