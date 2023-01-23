package frc.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice
import com.ctre.phoenix.motorcontrol.can.TalonFX
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import cshcyberhawks.swolib.hardware.AnalogTurnEncoder
import cshcyberhawks.swolib.hardware.GenericTurnEncoder
import cshcyberhawks.swolib.hardware.TalonFXEncoder
import cshcyberhawks.swolib.math.Polar
import edu.wpi.first.math.MathUtil
import edu.wpi.first.math.controller.PIDController
import frc.robot.Constants
import kotlin.math.abs


class SwerveWheel(turnPort: Int, drivePort: Int, private val turnEncoderPort: Int) {
    private var turnMotor: TalonSRX = TalonSRX(turnPort)
    private var driveMotor: TalonFX = TalonFX(drivePort)
    val turnEncoder: GenericTurnEncoder = AnalogTurnEncoder(turnEncoderPort, Constants.turnEncoderOffsets[turnEncoderPort])
    private var driveEncoder: TalonFXEncoder = TalonFXEncoder(driveMotor)

    private var oldAngle = 0.0

    // below is in m / 20 ms
    private var maxAcceleration = .01
    private var lastSpeed = 0.0

    private var turnValue = 0.0
    private var currentDriveSpeed = 0.0
    private var rawTurnValue = 0.0

    private var turnPidController: PIDController = PIDController(.01, 0.0, 0.0)
//    private var drivePidController: PIDController = PIDController(0.01, 0.0, 0.0)
//    private var speedPID: PIDController = PIDController(0.03, 0.0, 0.0)

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

    private fun wrapAroundAngles(input: Double): Double {
        return if (input < 0) 360 + input else input
    }

    fun convertToMetersPerSecond(rpm: Double): Double {
        val radius = 0.0505
        // Gear ratio is 7:1
        return 2 * Math.PI * radius / 60 * (rpm / 7)
    }

    fun convertToMetersPerSecondFromSecond(rps: Double): Double {
        val radius = 0.0505
        return 2 * Math.PI * radius * (rps / 7)
    }

    fun convertToWheelRotations(meters: Double): Double {
        val wheelConstant: Double = 2 * Math.PI * 0.0505 / 60
        return 7 * meters / wheelConstant
    }

    fun getCurrentDriveSpeed(): Double {
        val driveVelocity = driveEncoder.getVelocity()
        return convertToMetersPerSecondFromSecond(driveVelocity)
    }

    fun getTurnValue(): Double {
        return wrapAroundAngles(turnEncoder.get())
    }

    fun getWheelVector(): Polar = Polar(getTurnValue(), getCurrentDriveSpeed())

    fun drive(speed: Double, angle: Double) {
        var speed = speed
        var angle = angle
        oldAngle = angle

        currentDriveSpeed = getCurrentDriveSpeed()
        turnValue = getTurnValue()

        // SmartDashboard.putNumber("$m_turnEncoderPort wheel rotations", driveVelocity)
        rawTurnValue = turnEncoder.get()
        angle = wrapAroundAngles(angle)

        // Optimization Code stolen from
        // https://github.com/Frc2481/frc-2015/blob/master/src/Components/SwerveModule.cpp
        if (abs(angle - turnValue) > 90 && abs(angle - turnValue) < 270) {
            angle = ((angle.toInt() + 180) % 360).toDouble()
            speed = -speed
        }

        // if (mode == "tele") {
        if (abs(speed - lastSpeed) > maxAcceleration) {
            speed = if (speed > lastSpeed) {
                lastSpeed + maxAcceleration
            } else {
                lastSpeed - maxAcceleration
            }
        }
        // }
        lastSpeed = speed
        speed = convertToMetersPerSecond(speed * 5000.0) // Converting the speed to m/s with a max rpm of 5000 (GEar
        // ratio is 7:1)

        val turnPIDOutput = turnPidController.calculate(turnValue, angle)

        // maybe reason why gradual deceleration isn't working is because the PID
        // controller is trying to slow down by going opposite direction instead of
        // just letting wheels turn? maybe we need to skip the PID for slowing down?
        // maybe needs more tuning?
//        val drivePIDOutput = drivePidController.calculate(currentDriveSpeed, speed)

        // SmartDashboard.putNumber(m_turnEncoderPort + " pid value", drivePIDOutput)

        // double driveFeedForwardOutput = driveFeedforward.calculate(currentDriveSpeed,
        // speed)

        // SmartDashboard.putNumber(
        //     "$turnEncoderPort currentDriveSpeed", currentDriveSpeed
        // )
        // SmartDashboard.putNumber(m_turnEncoderPort + " turn set", turnPIDOutput)

        // SmartDashboard.putNumber(m_turnEncoderPort + " driveSet", (speed / 3.777) +
        // drivePIDOutput)
        // SmartDashboard.putNumber(turnEncoderPort.toString() + " drivespeed", getCurrentDriveSpeed());
        // 70% speed is about 5.6 feet/second
        driveMotor[ControlMode.PercentOutput] = MathUtil.clamp(speed / 4.00 /* + drivePIDOutput */, -1.0, 1.0)
        if (!turnPidController.atSetpoint()) {
            turnMotor[ControlMode.PercentOutput] = MathUtil.clamp(turnPIDOutput, -1.0, 1.0)
        }
    }

    fun preserveAngle() {
        drive(0.0, oldAngle)
    }

    fun getRawEncoder(): Double = turnEncoder.getRaw()

    fun kill() {
        driveMotor[ControlMode.PercentOutput] = 0.0
        turnMotor[ControlMode.PercentOutput] = 0.0
    }
}
