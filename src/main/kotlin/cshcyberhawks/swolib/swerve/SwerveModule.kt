package cshcyberhawks.swolib.swerve

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.can.TalonFX
import cshcyberhawks.swolib.hardware.AnalogTurnEncoder
import cshcyberhawks.swolib.hardware.TalonFXDriveEncoder
import cshcyberhawks.swolib.hardware.interfaces.GenericDriveMotor
import cshcyberhawks.swolib.hardware.interfaces.GenericTurnMotor
import cshcyberhawks.swolib.math.AngleCalculations
import cshcyberhawks.swolib.math.Coordinate
import edu.wpi.first.math.MathUtil
import edu.wpi.first.math.controller.PIDController

/**
 * A class used to encapsulate each individual swerve module in the swerve drive train. This class
 * directly constrols each module's PIDs, motors, and encoders. This module is set up to use a
 * single falcon 500 motor for driving and any motor that is controlled by a TalonSRX for turning.
 * The module uses the falcon's builtin encoders and PIDs. The module requires an analog encoder and
 * TalonSRX motor controller for the turning motor.
 *
 * @property turnMotor The turning motor controller - must
 *
 * @property dirveMotor The Falcon500 drive motor controller
 *
 * @property AnalogTurnEncoder The analog turn encoder used to track the module's wheel angle
 *
 * @property drivePIDF The F value for the Falcon500's builtin PID
 *
 * @property drivePID A PID used to set the values for the Falcon500's builtin PID
 *
 * @property turnPID The PID controller used on the turning motor
 *
 * @property wheelRadius The radius of the wheel on your swerve module (in meters)
 *
 * @property gearRatio The gear ratio on your swerve module
 *
 * @property maxSpeed the maximum speed of your swerve module assuming no constrictions such as
 * friction
 */
class SwerveModule(
        var turnMotor: GenericTurnMotor,
        var driveMotor: GenericDriveMotor,
        var drivePID: PIDController,
        var turnPID: PIDController,
        val wheelRadius: Double,
        val gearRatio: Double,
        val maxSpeed: Double,
) {
    private var oldAngle: Double = 0.0

    private fun convertToMetersPerSecond(rpm: Double): Double {
        return ((2 * Math.PI * wheelRadius) / 60) * (rpm / gearRatio)
    }

    private fun convertToMetersPerSecondFromSecond(rps: Double): Double {
        return (2 * Math.PI * wheelRadius * (rps / gearRatio))
    }

    private fun convertToWheelRotations(meters: Double): Double {
        val wheelConstant = 2 * Math.PI * wheelRadius / 60
        return 7 * meters / wheelConstant
    }

    fun preserveAngle() {
        drive(0.0, oldAngle)
    }

    fun kill() {
        driveMotor[ControlMode.PercentOutput] = 0.0
        turnMotor[ControlMode.PercentOutput] = 0.0
    }

    /**
     * The function used to control the movement of the wheels. This is called by the
     * SwerveDriveTrain class.
     *
     * @param inputSpeed The desired speed of the wheel.
     *
     * @param inputAngle The desired angle of the wheel.
     */
    fun drive(inputSpeed: Double, inputAngle: Double) {
        var speed = inputSpeed
        var angle = AngleCalculations.wrapAroundAngles(inputAngle)

        oldAngle = angle

        val turnValue = AngleCalculations.wrapAroundAngles(turnMotor.getTurnValue())

        angle = AngleCalculations.optimizeAngle(angle, turnValue)
        if (angle != oldAngle) {
            speed *= -1
        }

        speed *= convertToWheelRotations(maxSpeed)

        val drivePIDOutput = drivePID.calculate(driveMotor.getVelocity(), speed)

        val turnPIDOutput = turnPID.calculate(turnValue, angle)

        driveMotor[ControlMode.PercentOutput] = MathUtil.clamp((convertToMetersPerSecond(speed) / maxSpeed) + drivePIDOutput, -1.0, 1.0)
        if (!turnPID.atSetpoint()) {
            turnMotor[ControlMode.PercentOutput] = MathUtil.clamp(turnPIDOutput, -1.0, 1.0)
        }
    }

    /**
     * The function used to control the movement of the wheels. This is called by the SwerveDriveTrain class.
     *
     * @param input A polar vector with the desired speed and angle for the wheel.
     */
    fun drive(input: Coordinate) {
        drive(input.r, input.theta)
    }
}
