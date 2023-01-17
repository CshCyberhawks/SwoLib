package cshcyberhawks.swolib.swerve.configurations.fourwheelconfiguration

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
    var speedConfiguration: FourWheelSpeedConfiguration = FourWheelSpeedConfiguration(),
    var angleConfiguration: FourWheelAngleConfiguration = FourWheelAngleConfiguration()
) {
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