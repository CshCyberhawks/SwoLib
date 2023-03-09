package cshcyberhawks.swolib.swerve

import cshcyberhawks.swolib.hardware.interfaces.GenericGyro
import cshcyberhawks.swolib.math.Polar
import cshcyberhawks.swolib.math.Vector2
import cshcyberhawks.swolib.swerve.configurations.FourWheelSwerveConfiguration
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.SubsystemBase
import kotlin.math.abs

class SwerveDriveTrain(
    val swerveConfiguration: FourWheelSwerveConfiguration,
    private val gyro: GenericGyro
) : SubsystemBase() {
    companion object {
        fun normalizeWheelSpeeds(
            wheelVectors: Array<Double>,
            distanceFromZero: Double
        ): Array<Double> {
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

    init {
        gyro.setYawOffset()
    }

    fun fieldOriented(input: Vector2, gyroAngle: Double): Vector2 {
        val polar = Polar.fromVector2(input)
        polar.theta -= gyroAngle
        return Vector2.fromPolar(polar)
    }

    fun calculateDrive(
        driveCord: Vector2,
        twistCord: Vector2,
        disableFieldOrientation: Boolean = false
    ): Polar {
        var driveCoordinate = driveCord
        if (!disableFieldOrientation) {
            driveCoordinate = fieldOriented(driveCord, gyro.getYaw())
        }

        return Polar.fromVector2(
            Vector2(driveCoordinate.x + twistCord.x, driveCoordinate.y + twistCord.y)
        )
    }

    fun drive(input: Vector2, inputTwist: Double, disableFieldOrientation: Boolean = false) {
        if (input == Vector2() && inputTwist == 0.0) {
            swerveConfiguration.preserveWheelAngles()
            return
        }

        val frontRightVector =
            calculateDrive(
                input,
                Vector2.fromPolar(
                    Polar(
                        swerveConfiguration.angleConfiguration.frontRight,
                        inputTwist *
                                swerveConfiguration.speedConfiguration.frontRight
                    )
                ),
                disableFieldOrientation

            )
        val frontLeftVector =
            calculateDrive(
                input,
                Vector2.fromPolar(
                    Polar(
                        swerveConfiguration.angleConfiguration.frontLeft,
                        inputTwist *
                                swerveConfiguration.speedConfiguration.frontLeft
                    )
                ),
                disableFieldOrientation

            )
        val backRightVector =
            calculateDrive(
                input,
                Vector2.fromPolar(
                    Polar(
                        swerveConfiguration.angleConfiguration.backRight,
                        inputTwist *
                                swerveConfiguration.speedConfiguration.backRight
                    )
                ),
                disableFieldOrientation

            )
        val backLeftVector =
            calculateDrive(
                input,
                Vector2.fromPolar(
                    Polar(
                        swerveConfiguration.angleConfiguration.backLeft,
                        inputTwist * swerveConfiguration.speedConfiguration.backLeft
                    )
                ),
                disableFieldOrientation
            )
        val frontRightSpeed = frontRightVector.r
        val frontLeftSpeed = frontLeftVector.r
        val backRightSpeed = backRightVector.r
        val backLeftSpeed = backLeftVector.r
        val frontRightAngle = frontRightVector.theta
        val frontLeftAngle = frontLeftVector.theta
        val backRightAngle = backRightVector.theta
        val backLeftAngle = backLeftVector.theta
        var wheelSpeeds = arrayOf(frontRightSpeed, frontLeftSpeed, backRightSpeed, backLeftSpeed)
        wheelSpeeds = normalizeWheelSpeeds(wheelSpeeds, 1.0)

        SmartDashboard.putNumber("Drive in x", input.x)
        SmartDashboard.putNumber("Drive in y", input.y)

        swerveConfiguration.backRight.drive(wheelSpeeds[2], backRightAngle)
        swerveConfiguration.backLeft.drive(wheelSpeeds[3], backLeftAngle)
        swerveConfiguration.frontRight.drive(wheelSpeeds[0], frontRightAngle)
        swerveConfiguration.frontLeft.drive(wheelSpeeds[1], frontLeftAngle)
    }

    fun debug() {
        logEncoderValues()
        // put debug stuff here
    }

    fun logEncoderValues() {
        val vals =
            arrayOf(
                swerveConfiguration.frontRight.getRawEncoder(),
                swerveConfiguration.frontLeft.getRawEncoder(),
                swerveConfiguration.backLeft.getRawEncoder(),
                swerveConfiguration.backRight.getRawEncoder()
            )
        SmartDashboard.putString("Encoder values", vals.joinToString(", "))
    }
}
