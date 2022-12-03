package cshcyberhawks.swolib.swerve

import cshcyberhawks.swolib.hardware.GenericGyro
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
class SwerveDriveTrain(var swerveConfiguration: FourWheelSwerveConfiguration, var gyro: GenericGyro) {
    //TODO: Throttle things
    var throttle = 0.5

    private fun normalizeWheelSpeeds(wheelVectors: Array<Coordinate>, maxSpeed: Double, minSpeed: Double): Array<Coordinate> {
        var min = wheelVectors[0].r
        var max = wheelVectors[0].r

        for (wheelVector in wheelVectors) {
            if (wheelVector.r < min) {
                min = wheelVector.r
            } else if (wheelVector.r > max) {
                max = wheelVector.r
            }
        }

        // TODO: This is spaghetti
        val divSpeed = if (abs(min) > max) abs(min) else max
        val highestSpeed = if (max > maxSpeed) maxSpeed else max
        val lowestSpeed = if (min < minSpeed) minSpeed else min

        for (i in wheelVectors.indices) {
            if  (max > maxSpeed && wheelVectors[i].r > 0) {
                wheelVectors[i].r = wheelVectors[i].r / divSpeed * highestSpeed
            } else if (min < minSpeed && wheelVectors[i].r < 0) {
                wheelVectors[i].r = wheelVectors[i].r / -divSpeed * lowestSpeed
            }
        }

        return wheelVectors
    }

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

        // The random numbers are the angle that the wheels need to turn to for the robot to turn
        var frontRightVector = calculateDrive(input, Coordinate.fromPolar(swerveConfiguration.angleConfiguration.frontRight, inputTwist * swerveConfiguration.speedConfiguration.frontRight))
        var frontLeftVector = calculateDrive(input, Coordinate.fromPolar(swerveConfiguration.angleConfiguration.frontLeft, inputTwist * swerveConfiguration.speedConfiguration.frontLeft))
        var backRightVector = calculateDrive(input, Coordinate.fromPolar(swerveConfiguration.angleConfiguration.backRight, inputTwist * swerveConfiguration.speedConfiguration.backRight))
        var backLeftVector = calculateDrive(input, Coordinate.fromPolar(swerveConfiguration.angleConfiguration.backLeft, inputTwist * swerveConfiguration.speedConfiguration.backLeft))

        val wheelVectors = normalizeWheelSpeeds(arrayOf(frontRightVector, frontLeftVector, backRightVector, backLeftVector), 1.0, -1.0)

        frontRightVector = wheelVectors[0]
        frontLeftVector = wheelVectors[1]
        backRightVector = wheelVectors[2]
        backLeftVector = wheelVectors[3]

        swerveConfiguration.frontRight.drive(frontRightVector)
        swerveConfiguration.frontLeft.drive(frontLeftVector)
        swerveConfiguration.backRight.drive(backRightVector)
        swerveConfiguration.backLeft.drive(backLeftVector)
    }
}