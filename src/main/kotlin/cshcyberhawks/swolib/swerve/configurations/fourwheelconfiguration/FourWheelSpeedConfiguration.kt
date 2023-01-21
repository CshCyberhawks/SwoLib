package cshcyberhawks.swolib.swerve.configurations.fourwheelconfiguration

/**
 * A data class containing the speed of the wheels in a four wheel swerve configuration
 *
 * @property frontRight The speed of the front right swerve module
 * @property frontLeft The speed of the front left swerve module
 * @property backRight The speed of the back right swerve module
 * @property backLeft The speed of the back left swerve module
 */
data class FourWheelSpeedConfiguration(
    var frontRight: Double = 0.5,
    var frontLeft: Double = 0.5,
    var backRight: Double = 0.5,
    var backLeft: Double = 0.5
)