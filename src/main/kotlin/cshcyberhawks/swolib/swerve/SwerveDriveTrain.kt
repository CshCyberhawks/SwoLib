package cshcyberhawks.swolib.swerve

import cshcyberhawks.swolib.hardware.NavXGyro
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
    var throttle = 0.5

    private fun normalizeWheelSpeeds(wheelVectors: Array<Polar>, maxSpeed: Double, minSpeed: Double): Array<Polar> {
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

    private fun fieldOriented(x: Double, y: Double, gyroAngle: Double): Vector2 {
        val polar = CoordinateCalculations.cartesianToPolar(x, y)

        return CoordinateCalculations.polarToCartesian(polar.theta + gyroAngle, polar.r)
    }

    private fun calculateDrive(inputX: Double, inputY: Double, inputTwist: Double, twistConstant: Double, twistMultiplier: Double): Polar {
        val driveCoordinate = fieldOriented(inputX, inputY, gyro.getAngle().toDouble())

        val twistCoordinate = CoordinateCalculations.polarToCartesian(twistConstant, inputTwist * twistMultiplier)

        return CoordinateCalculations.cartesianToPolar(driveCoordinate.x + twistCoordinate.x, driveCoordinate.y + twistCoordinate.y)
    }

    fun drive(inputX: Number, inputY: Number, inputTwist: Number) {
        if (inputX == 0 && inputY == 0 && inputTwist == 0) {
            // Leave the angles alone if there is no input
            // Without this the wheel flip back to their default positions
            swerveConfiguration.preserveAngles()
            return
        }

        // The random numbers are the angle that the wheels need to turn to for the robot to turn
        var frontRightVector = calculateDrive(inputX.toDouble(), inputY.toDouble(), inputTwist.toDouble(), swerveConfiguration.angleConfiguration.frontRight.toDouble(), swerveConfiguration.speedConfiguration.frontRight.toDouble())
        var frontLeftVector = calculateDrive(inputX.toDouble(), inputY.toDouble(), inputTwist.toDouble(), swerveConfiguration.angleConfiguration.frontLeft.toDouble(), swerveConfiguration.speedConfiguration.frontLeft.toDouble())
        var backRightVector = calculateDrive(inputX.toDouble(), inputY.toDouble(), inputTwist.toDouble(), swerveConfiguration.angleConfiguration.backRight.toDouble(), swerveConfiguration.speedConfiguration.backRight.toDouble())
        var backLeftVector = calculateDrive(inputX.toDouble(), inputY.toDouble(), inputTwist.toDouble(), swerveConfiguration.angleConfiguration.backLeft.toDouble(), swerveConfiguration.speedConfiguration.backLeft.toDouble())

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