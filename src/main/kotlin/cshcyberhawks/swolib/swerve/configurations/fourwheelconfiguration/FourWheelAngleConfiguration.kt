package cshcyberhawks.swolib.swerve.configurations.fourwheelconfiguration

/**
 * A data class containing the angles of the wheels in a four wheel swerve configuration
 *
 * @property frontRight The angle of the front right swerve module
 * @property frontLeft The angle of the front left swerve module
 * @property backRight The angle of the back right swerve module
 * @property backLeft The angle of the back left swerve module
 */
data class FourWheelAngleConfiguration(
    var frontRight: Double = -135.0,
    var frontLeft: Double = -45.0,
    var backRight: Double = 135.0,
    var backLeft: Double = 45.0
)
