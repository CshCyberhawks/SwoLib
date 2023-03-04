package cshcyberhawks.swolib.swerve.configurations

import cshcyberhawks.swolib.math.Polar
import cshcyberhawks.swolib.swerve.SwerveWheel

data class FourWheelAngleConfiguration(
    val frontRight: Double = 45.0,
    val frontLeft: Double = 135.0,
    val backRight: Double = -45.0,
    val backLeft: Double = -135.0
)

data class FourWheelSpeedConfiguration(
    val frontRight: Double = 0.5,
    val frontLeft: Double = 0.5,
    val backRight: Double = 0.5,
    val backLeft: Double = 0.5
)

class FourWheelSwerveConfiguration(
    val frontRight: SwerveWheel,
    val frontLeft: SwerveWheel,
    val backRight: SwerveWheel,
    val backLeft: SwerveWheel,
    val angleConfiguration: FourWheelAngleConfiguration = FourWheelAngleConfiguration(),
    val speedConfiguration: FourWheelSpeedConfiguration = FourWheelSpeedConfiguration()
) {
    fun preserveWheelAngles() {
        frontRight.preserveAngle()
        frontLeft.preserveAngle()
        backRight.preserveAngle()
        backLeft.preserveAngle()
    }

    fun getWheelVectors(): Array<Polar> = arrayOf(
        frontRight.getWheelVector(),
        frontLeft.getWheelVector(),
        backRight.getWheelVector(),
        backLeft.getWheelVector()
    )
}
