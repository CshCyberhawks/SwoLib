package cshcyberhawks.swolib.swerve.configurations

import cshcyberhawks.swolib.swerve.SwerveModule

class FourWheelSwerveConfiguration(var frontRight: SwerveModule, var frontLeft: SwerveModule, var backRight: SwerveModule, var backLeft: SwerveModule) {
    fun preserveAngles() {
        frontRight.preserveAngle()
        backLeft.preserveAngle()
        backRight.preserveAngle()
        frontLeft.preserveAngle()
    }
}