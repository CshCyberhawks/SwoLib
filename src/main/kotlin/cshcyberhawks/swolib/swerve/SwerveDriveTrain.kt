package frc.robot.subsystems

import cshcyberhawks.swolib.hardware.GenericGyro
import cshcyberhawks.swolib.math.Polar
import cshcyberhawks.swolib.math.Vector2
import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.util.WPIUtilJNI
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.Constants
import frc.robot.util.*
import java.lang.Double.max
import java.lang.Double.min
import kotlin.math.*

class SwerveDriveTrain(val gyro: GenericGyro) : SubsystemBase() {
    companion object {
        fun normalizeWheelSpeeds(wheelVectors: Array<Double>, distanceFromZero: Double): Array<Double> {
            var max = abs(wheelVectors[0])

            for (wheelVector in wheelVectors) {
                if (abs(wheelVector) > max) {
                    max = abs(wheelVector)
                }
            }

            val maxSpeed = if (max > distanceFromZero) max else distanceFromZero

            for (i in wheelVectors.indices) {
                wheelVectors[i] = wheelVectors[i] / maxSpeed * distanceFromZero
            }

            return wheelVectors
        }
    }

    var backLeft: SwerveWheel =
            SwerveWheel(
                    Constants.backLeftTurnMotor,
                    Constants.backLeftDriveMotor,
                    Constants.backLeftEncoder
            )
    var backRight: SwerveWheel =
            SwerveWheel(
                    Constants.backRightTurnMotor,
                    Constants.backRightDriveMotor,
                    Constants.backRightEncoder
            )
    var frontLeft: SwerveWheel =
            SwerveWheel(
                    Constants.frontLeftTurnMotor,
                    Constants.frontLeftDriveMotor,
                    Constants.frontLeftEncoder
            )
    var frontRight: SwerveWheel =
            SwerveWheel(
                    Constants.frontRightTurnMotor,
                    Constants.frontRightDriveMotor,
                    Constants.frontRightEncoder
            )


    init {
        gyro.setYawOffset()
    }

    fun fieldOriented(input: Vector2, gyroAngle: Double): Vector2 {
        val polar = Polar.fromVector2(input)
        polar.theta += gyroAngle
        return Vector2.fromPolar(polar)
    }

    fun calculateDrive(
            driveCord: Vector2,
            twistCord: Vector2,
    ): Polar {
        // X is 0 and Y is 1
        // Gets the cartesian coordinate of the robot's joystick translation inputs
        //        SmartDashboard.putBoolean("Field Oriented", fieldOrientedEnabled)
        val driveCoordinate = fieldOriented(driveCord, gyro.getYaw())
        // Turns the twist constant + joystick twist input into a cartesian coordinates

        // Args are theta, r
        // Vector math adds the translation and twisting cartesian coordinates before
        // turning them into polar and returning
        // can average below instead of add - need to look into it
        return Polar.fromVector2(
                Vector2(driveCoordinate.x + twistCord.x,
                driveCoordinate.y + twistCord.y)
        )
    }

    fun drive(
            input: Vector2,
            inputTwist: Double,
    ) {
        if (input == Vector2() && inputTwist == 0.0) {
            backRight.preserveAngle()
            backLeft.preserveAngle()
            frontRight.preserveAngle()
            frontLeft.preserveAngle()
            return
        }

        val twistMult = 0.5;
        val frontRightVector =
                calculateDrive(
                        input,
                    Vector2.fromPolar( Polar(
                            45.0,
                inputTwist * twistMult)
                ))
        val frontLeftVector =
                calculateDrive(
                    input,
                    Vector2.fromPolar(Polar(
                        135.0,
                        inputTwist * twistMult))
                )
        val backRightVector =
                calculateDrive(
                    input,
                    Vector2.fromPolar(Polar(
                        -45.0,
                        inputTwist* twistMult ))
                )
        val backLeftVector =
                calculateDrive(
                    input,
                    Vector2.fromPolar(Polar(
                        -135.0,
                        inputTwist*twistMult))
                )

        var wheelSpeeds =
                arrayOf(frontRightVector.r, frontLeftVector.r, backRightVector.r, backLeftVector.r)
        wheelSpeeds = normalizeWheelSpeeds(wheelSpeeds, 1.0)

        backRight.drive(wheelSpeeds[2], backRightVector.theta)
        backLeft.drive(wheelSpeeds[3], backLeftVector.theta)
        frontRight.drive(wheelSpeeds[0], frontRightVector.theta)
        frontLeft.drive(wheelSpeeds[1], frontLeftVector.theta)
    }

    fun logEncoderValues() {
        val vals = arrayOf(frontRight.getRawEncoder(), frontLeft.getRawEncoder(), backLeft.getRawEncoder(), backRight.getRawEncoder())
        SmartDashboard.putString("Encoder values", vals.joinToString(", "))
    }
}
