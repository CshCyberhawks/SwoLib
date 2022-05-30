package cshcyberhawks.swolib.swerve

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX
import cshcyberhawks.swolib.hardware.AnalogTurnEncoder
import cshcyberhawks.swolib.hardware.TalonFXDriveEncoder
import cshcyberhawks.swolib.math.AngleCalculations
import edu.wpi.first.math.MathUtil
import edu.wpi.first.math.controller.PIDController

class SwerveModule(
        var turnMotor: TalonSRX,
        var driveMotor: WPI_TalonFX,
        var turnEncoder: AnalogTurnEncoder,
        var drivePIDF: Number,
        var drivePID: PIDController,
        var turnPID: PIDController,
        val wheelRadius: Number,
        val gearRatio: Number,
        val maxSpeed: Number
) {
    private var oldAngle: Double = 0.0

    var driveEncoder: TalonFXDriveEncoder = TalonFXDriveEncoder(driveMotor)

    init {
        driveMotor.config_kF(0, drivePIDF.toDouble())
        driveMotor.config_kP(0, drivePID.p)
        driveMotor.config_kI(0, drivePID.i)
        driveMotor.config_kD(0, drivePID.d)

        driveMotor.setNeutralMode(NeutralMode.Brake)
    }

    private fun convertToMetersPerSecond(rpm: Double): Double {
        return ((2 * Math.PI * wheelRadius.toDouble()) / 60) * (rpm / gearRatio.toDouble())
    }

    // I hate this
    private fun convertToMetersPerSecondFromSecond(rps: Double): Double {
        return (2 * Math.PI * wheelRadius.toDouble()) * (rps / gearRatio.toDouble())
    }

    private fun convertToWheelRotations(meters: Double): Double {
        val wheelConstant = 2 * Math.PI * wheelRadius.toDouble() / 60
        return 7 * meters / wheelConstant
    }

    fun preserveAngle() {
        drive(0, oldAngle)
    }

    fun kill() {
        driveMotor[ControlMode.PercentOutput] = 0.0
        turnMotor[ControlMode.PercentOutput] = 0.0
    }

    fun drive(inputSpeed: Number, inputAngle: Number) {
        var speed = inputSpeed.toDouble()
        var angle = AngleCalculations.wrapAroundAngles(inputAngle).toDouble()

        oldAngle = angle

        val turnValue = AngleCalculations.wrapAroundAngles(turnEncoder.get())

        angle = AngleCalculations.optimizeAngle(angle, turnValue).toDouble()
        if (angle != oldAngle) {
            speed *= -1
        }

        speed = convertToMetersPerSecond(speed * convertToWheelRotations(maxSpeed.toDouble()))

        val turnPIDOutput = turnPID.calculate(turnValue.toDouble(), angle)

        driveMotor.set(
                ControlMode.PercentOutput,
                MathUtil.clamp(speed / maxSpeed.toDouble(), -1.0, 1.0)
        )
        if (!turnPID.atSetpoint()) {
            turnMotor.set(ControlMode.PercentOutput, MathUtil.clamp(turnPIDOutput, -1.0, 1.0))
        }
    }
}
