package cshcyberhawks.swolib.swerve

import cshcyberhawks.swolib.hardware.enums.MotorNeutralMode
import cshcyberhawks.swolib.hardware.interfaces.GenericDriveMotor
import cshcyberhawks.swolib.hardware.interfaces.GenericTurnMotor
import cshcyberhawks.swolib.math.AngleCalculations
import cshcyberhawks.swolib.math.Polar
import cshcyberhawks.swolib.swerve.configurations.SwerveModuleConfiguration
import edu.wpi.first.math.MathUtil
import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import kotlin.math.abs


class SwerveWheel(
    private val driveMotor: GenericDriveMotor,
    private val turnMotor: GenericTurnMotor,
    private val drivePID: PIDController,
    private val turnPID: PIDController,
    private val configuration: SwerveModuleConfiguration
) {
    private var oldAngle = 0.0

    init {
        driveMotor.setNeutralMode(MotorNeutralMode.Brake)
        turnPID.setTolerance(.4)
        turnPID.enableContinuousInput(0.0, 360.0)
    }

    private fun rotationsPerSecondToMetersPerSecond(rps: Double): Double {
        return 2 * Math.PI * configuration.wheelRadius * (rps / configuration.gearRatio)
    }

    private fun getCurrentDriveSpeed(): Double {
        val driveVelocity = driveMotor.getVelocity()
        return rotationsPerSecondToMetersPerSecond(driveVelocity)
    }

    private fun getTurnValue(): Double {
        return AngleCalculations.wrapAroundAngles(turnMotor.get())
    }

    fun getWheelVector(): Polar = Polar(getTurnValue(), getCurrentDriveSpeed())

    fun drive(inputSpeed: Double, inputAngle: Double) {
        var speed = inputSpeed
        var angle = AngleCalculations.wrapAroundAngles(inputAngle)
        oldAngle = angle

        val turnValue = getTurnValue()

        angle = AngleCalculations.optimizeAngle(angle, turnValue)
        if (angle != oldAngle) {
            speed *= -1
        }

        speed *= configuration.maxSpeed

        val drivePIDOutput = drivePID.calculate(getCurrentDriveSpeed(), speed)

        val turnPIDOutput = turnPID.calculate(turnValue, angle)
        SmartDashboard.putNumber("angle in wheel for " + this.turnMotor.encoderPort, angle)

        driveMotor.setPercentOutput(MathUtil.clamp(speed / configuration.maxSpeed + drivePIDOutput, -1.0, 1.0))
        if (abs(turnValue - angle) >= .4) {
            turnMotor.setPercentOutput(MathUtil.clamp(-turnPIDOutput, -1.0, 1.0))
        }
    }

    fun preserveAngle() {
        drive(0.0, oldAngle)
    }

    fun getRawEncoder(): Double = turnMotor.getRaw()
}
