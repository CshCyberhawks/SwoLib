package cshcyberhawks.swolib.swerve

import cshcyberhawks.swolib.hardware.NavXGyro
import cshcyberhawks.swolib.math.Coordinate
import cshcyberhawks.swolib.swerve.configurations.fourwheelconfiguration.FourWheelSwerveConfiguration
import kotlin.math.abs

/**
 * A class to encapsulate an entire 4 wheeled swerve drive train. The swerve modules are SwoLib
 * module and this drive train uses a NavX gyro for field orientation.
 *
 * @property swerveConfiguration A class that encapsulates the swerve modules used in the drive train.
 *
 * @property gyro The navx gyro used by the swerve drive train for field orientation.
 *
 */
class SwerveDriveTrain(var swerveConfiguration: FourWheelSwerveConfiguration, var gyro: NavXGyro) {
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

    var throttle = 0.5

    private fun fieldOriented(coord: Coordinate, gyroAngle: Double): Coordinate {
        coord.theta += gyroAngle
        return coord
    }

    private fun calculateDrive(driveCoord: Coordinate, twistCoord: Coordinate): Coordinate {
        return fieldOriented(driveCoord, gyro.getAngle()) + twistCoord
    }

    fun drive(input: Coordinate, inputTwist: Double) {
        if (input == Coordinate(0.0, 0.0) && inputTwist == 0.0) {
            // Leave the angles alone if there is no input
            // Without this the wheel flip back to their default positions
            swerveConfiguration.preserveAngles()
            return
        }

        input *= throttle

        // The random numbers are the angle that the wheels need to turn to for the robot to turn
        val frontRightVector = calculateDrive(input, Coordinate.fromPolar(swerveConfiguration.angleConfiguration.frontRight, inputTwist * swerveConfiguration.speedConfiguration.frontRight))
        val frontLeftVector = calculateDrive(input, Coordinate.fromPolar(swerveConfiguration.angleConfiguration.frontLeft, inputTwist * swerveConfiguration.speedConfiguration.frontLeft))
        val backRightVector = calculateDrive(input, Coordinate.fromPolar(swerveConfiguration.angleConfiguration.backRight, inputTwist * swerveConfiguration.speedConfiguration.backRight))
        val backLeftVector = calculateDrive(input, Coordinate.fromPolar(swerveConfiguration.angleConfiguration.backLeft, inputTwist * swerveConfiguration.speedConfiguration.backLeft))

        val wheelVectors = normalizeWheelSpeeds(arrayOf(frontRightVector.r, frontLeftVector.r, backRightVector.r, backLeftVector.r), 1.0)

        frontRightVector.r = wheelVectors[0]
        frontLeftVector.r = wheelVectors[1]
        backRightVector.r = wheelVectors[2]
        backLeftVector.r = wheelVectors[3]

        swerveConfiguration.frontRight.drive(frontRightVector)
        swerveConfiguration.frontLeft.drive(frontLeftVector)
        swerveConfiguration.backRight.drive(backRightVector)
        swerveConfiguration.backLeft.drive(backLeftVector)
    }
}