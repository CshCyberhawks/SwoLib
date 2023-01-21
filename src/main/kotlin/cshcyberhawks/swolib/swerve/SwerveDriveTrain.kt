package cshcyberhawks.swolib.swerve

import cshcyberhawks.swolib.hardware.GenericGyro
import cshcyberhawks.swolib.math.Coordinate
import cshcyberhawks.swolib.swerve.configurations.fourwheelconfiguration.FourWheelSwerveConfiguration
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
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

        fun calculateDrive(driveCoord: Coordinate, twistCoord: Coordinate, gyroAngle: Double): Coordinate {
//            return driveCoord.apply { theta += gyroAngle } + twistCoord
            return driveCoord + twistCoordgit 
        }
    }

    var throttle = 0.5

    fun drive(input: Coordinate, inputTwist: Double) {
        if (input == Coordinate(0.0, 0.0) && inputTwist == 0.0) {
            // Leave the angles alone if there is no input
            // Without this the wheel flip back to their default positions
            swerveConfiguration.preserveAngles()
            return
        }

        SmartDashboard.putNumber("Input X", input.x)
        SmartDashboard.putNumber("Input Y", input.y)
        SmartDashboard.putNumber("Input Twist", inputTwist)
        SmartDashboard.putNumber("Gyro Angle", gyro.getYaw())

        input *= throttle

        val gyroAngle = gyro.getYaw()

        val frontRightVector = calculateDrive(
            input,
            Coordinate.fromPolar(
                swerveConfiguration.angleConfiguration.frontRight,
                inputTwist * swerveConfiguration.speedConfiguration.frontRight
            ),
            gyroAngle
        )
        val frontLeftVector = calculateDrive(
            input,
            Coordinate.fromPolar(
                swerveConfiguration.angleConfiguration.frontLeft,
                inputTwist * swerveConfiguration.speedConfiguration.frontLeft
            ),
            gyroAngle
        )
        val backRightVector = calculateDrive(
            input,
            Coordinate.fromPolar(
                swerveConfiguration.angleConfiguration.backRight,
                inputTwist * swerveConfiguration.speedConfiguration.backRight
            ),
            gyroAngle
        )
        val backLeftVector = calculateDrive(
            input,
            Coordinate.fromPolar(
                swerveConfiguration.angleConfiguration.backLeft,
                inputTwist * swerveConfiguration.speedConfiguration.backLeft
            ),
            gyroAngle
        )

//        val wheelVectors = normalizeWheelSpeeds(
  val wheelVectors = arrayOf(frontRightVector.r, frontLeftVector.r, backRightVector.r, backLeftVector.r)
//            1.0
//        )

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