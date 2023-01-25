package frc.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice
import com.ctre.phoenix.motorcontrol.can.TalonFX
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import cshcyberhawks.swolib.hardware.AnalogTurnEncoder
import cshcyberhawks.swolib.hardware.GenericTurnEncoder
import cshcyberhawks.swolib.hardware.TalonFXEncoder
import cshcyberhawks.swolib.math.AngleCalculations
import cshcyberhawks.swolib.math.Polar
import cshcyberhawks.swolib.swerve.configurations.SwerveModuleConfiguration
import edu.wpi.first.math.MathUtil
import edu.wpi.first.math.controller.PIDController
import frc.robot.Constants
import kotlin.math.abs


class SwerveWheel(val driveMotor: TalonFX, val turnMotor: TalonSRX, val turnEncoder: GenericTurnEncoder, val drivePID: PIDController, val turnPID: PIDController, val configuration: SwerveModuleConfiguration) {
    private var driveEncoder: TalonFXEncoder = TalonFXEncoder(driveMotor)

    private var oldAngle = 0.0

    private var turnPidController: PIDController = PIDController(.01, 0.0, 0.0)

    init {
        driveMotor.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, 0)
        driveMotor.config_kF(0, 0.0)
        driveMotor.config_kP(0, 0.01)
        driveMotor.config_kI(0, 0.0)
        driveMotor.config_kD(0, 0.0)


        driveMotor.setNeutralMode(NeutralMode.Brake)
        turnPidController.setTolerance(1.0)
        turnPidController.enableContinuousInput(0.0, 360.0)
        driveMotor.inverted = turnEncoder.port == 1 || turnEncoder.port == 3
    }

    private fun rotationsPerSecondToMetersPerSecond(rps: Double): Double {
        return 2 * Math.PI * configuration.wheelRadius * (rps / configuration.gearRatio)
    }

    private fun getCurrentDriveSpeed(): Double {
        val driveVelocity = driveEncoder.getVelocity()
        return rotationsPerSecondToMetersPerSecond(driveVelocity)
    }

    private fun getTurnValue(): Double {
        return AngleCalculations.wrapAroundAngles(turnEncoder.get())
    }

    fun getWheelVector(): Polar = Polar(getTurnValue(), getCurrentDriveSpeed())

    fun drive(speed: Double, angle: Double) {
        var speed = speed
        var angle = AngleCalculations.wrapAroundAngles(angle)
        oldAngle = angle

        val turnValue = getTurnValue()

        angle = AngleCalculations.optimizeAngle(angle, turnValue)
        if (angle != oldAngle) {
            speed *= -1
        }

        speed *= configuration.maxSpeed

        val turnPIDOutput = turnPidController.calculate(turnValue, angle)

        driveMotor[ControlMode.PercentOutput] = MathUtil.clamp(speed / configuration.maxSpeed /* + drivePIDOutput */, -1.0, 1.0)
        if (!turnPidController.atSetpoint()) {
            turnMotor[ControlMode.PercentOutput] = MathUtil.clamp(turnPIDOutput, -1.0, 1.0)
        }
    }

    fun preserveAngle() {
        drive(0.0, oldAngle)
    }

    fun getRawEncoder(): Double = turnEncoder.getRaw()
}
