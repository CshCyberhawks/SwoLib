package cshcyberhawks.swolib.swerve

import cshcyberhawks.swolib.hardware.GenericGyro
import cshcyberhawks.swolib.math.MiscCalculations
import cshcyberhawks.swolib.math.Vector2
import cshcyberhawks.swolib.math.Vector3
import frc.robot.subsystems.SwerveDriveTrain
import kotlin.math.cos
import kotlin.math.sqrt

class SwerveOdometry(private var swerveDriveTrain: SwerveDriveTrain, private var gyro: GenericGyro) {
    var fieldPosition = Vector3()

    fun getVelocity(): Vector3 {
        var totalX = 0.0
        var totalY = 0.0

        val wheelVectors = swerveDriveTrain.getWheelVectors()
        for (wheel in wheelVectors) {
            val wheelVector = Vector2.fromPolar(wheel)
            totalX += wheelVector.x
            totalY += wheelVector.y
        }
        totalX /= wheelVectors.size
        totalY /= wheelVectors.size

        // Pitch and roll might be flipped
        val x = totalX * cos(gyro.getPitch())
        val y = totalY * cos(gyro.getRoll())
        val z = sqrt(totalX * totalX - x * x) + sqrt(totalY * totalY - y * y) // This BS is to avoid another trig call (they are slow)
        return Vector3(x, y, z)
    }

    fun updatePosition() {
        fieldPosition += getVelocity() * MiscCalculations.getCurrentTime()
    }
}