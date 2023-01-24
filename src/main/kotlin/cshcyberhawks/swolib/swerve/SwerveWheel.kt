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
import edu.wpi.first.math.MathUtil
import edu.wpi.first.math.controller.PIDController
import frc.robot.Constants
import java.lang.reflect.AnnotatedWildcardType
import kotlin.math.abs


class SwerveWheel(turnPort: Int, drivePort: Int, private val turnEncoderPort: Int) {
    private var turnMotor: TalonSRX = TalonSRX(turnPort)
    private var driveMotor: TalonFX = TalonFX(drivePort)
    val turnEncoder: GenericTurnEncoder = AnalogTurnEncoder(turnEncoderPort, Constants.turnEncoderOffsets[turnEncoderPort])
    private var driveEncoder: TalonFXEncoder = TalonFXEncoder(driveMotor)

    private var oldAngle = 0.0

    private var turnPidController: PIDController = PIDController(.01, 0.0, 0.0)

    init {
        driveMotor.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, 0)
        driveMotor.config_kF(0, 0.0)
        driveMotor.config_kP(0, 0.01)
        driveMotor.config_kI(0, 0.0)
        driveMotor.config_kD(0, 0.0)
        driveMotor.setStatusFramePeriod(4, 2800)
        driveMotor.setStatusFramePeriod(8, 2600)
        driveMotor.setStatusFramePeriod(14, 2400)
        driveMotor.setStatusFramePeriod(10, 100000)


        driveMotor.setNeutralMode(NeutralMode.Brake)
        turnPidController.setTolerance(1.0)
        turnPidController.enableContinuousInput(0.0, 360.0)
        driveMotor.inverted = turnEncoderPort == 1 || turnEncoderPort == 3
    }

    private fun rotationsPerSecondToMetersPerSecond(rps: Double): Double {
        val radius = 0.0505
        return 2 * Math.PI * radius * (rps / 7)
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
        var angle = angle
        oldAngle = angle

        val turnValue = getTurnValue()

        angle = AngleCalculations.wrapAroundAngles(angle)

        // Optimization Code stolen from
        // https://github.com/Frc2481/frc-2015/blob/master/src/Components/SwerveModule.cpp
        if (abs(angle - turnValue) > 90 && abs(angle - turnValue) < 270) {
            angle = ((angle.toInt() + 180) % 360).toDouble()
            speed = -speed
        }

        speed *= 4.00

        val turnPIDOutput = turnPidController.calculate(turnValue, angle)

        driveMotor[ControlMode.PercentOutput] = MathUtil.clamp(speed / 4.00 /* + drivePIDOutput */, -1.0, 1.0)
        if (!turnPidController.atSetpoint()) {
            turnMotor[ControlMode.PercentOutput] = MathUtil.clamp(turnPIDOutput, -1.0, 1.0)
        }
    }

    fun preserveAngle() {
        drive(0.0, oldAngle)
    }

    fun getRawEncoder(): Double = turnEncoder.getRaw()
}
