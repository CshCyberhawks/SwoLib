package cshcyberhawks.swolib.swerve.configurations

import cshcyberhawks.swolib.swerve.SwerveModule

/**
 * A class that encapsulates the swerve modules for a four wheel swerve configuration.
 *
 * @property frontRight The front right swerve module.
 *
 * @property frontLeft The front left swerve module.
 *
 * @property backRight The back right swerve module.
 *
 * @property backLeft The back left swerve module.
 *
 */
class FourWheelSwerveConfiguration(
    var frontRight: SwerveModule,
    var frontLeft: SwerveModule,
    var backRight: SwerveModule,
    var backLeft: SwerveModule,
    var frontRightTurnMultiplier: Number = 0.5,
    var frontLeftTurnMultiplier: Number = 0.5,
    var backRightTurnMultiplier: Number = 0.5,
    var backLeftTurnMultiplier: Number = 0.5,
    var frontRightTurnAngle: Number = 45,
    var frontLeftTurnAngle: Number = 135,
    var backRightTurnAngle: Number = -45,
    var backLeftTurnAngle: Number = -135) {


    /**
     * A function that allows you to preserve the current angle for all the swerve wheels in the configuration.
     */
    fun preserveAngles() {
        frontRight.preserveAngle()
        backLeft.preserveAngle()
        backRight.preserveAngle()
        frontLeft.preserveAngle()
    }
}