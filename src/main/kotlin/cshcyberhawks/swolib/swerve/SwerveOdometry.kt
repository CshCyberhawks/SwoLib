package cshcyberhawks.swolib.swerve

import cshcyberhawks.swolib.hardware.GenericGyro
import cshcyberhawks.swolib.math.MiscCalculations
import cshcyberhawks.swolib.math.Polar
import cshcyberhawks.swolib.math.Vector2
import cshcyberhawks.swolib.math.Vector3
import frc.robot.subsystems.SwerveDriveTrain
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class SwerveOdometry(private var swerveDriveTrain: SwerveDriveTrain, private var gyro: GenericGyro) {
    var fieldPosition = Vector3()
    var lastTime = MiscCalculations.getCurrentTime()

    fun getVelocity(): Vector3 {
        var total = Vector2()

        val wheelVectors = swerveDriveTrain.getWheelVectors()
        for (wheel in wheelVectors) {
            val wheelVector = Vector2.fromPolar(wheel)
            total += wheelVector
        }
        total /= wheelVectors.size

        val polar = Polar.fromVector2(total)
        polar.theta -= gyro.getYaw()
        total = Vector2.fromPolar(polar)

        // Pitch and roll might be flipped
        val x = total.x * cos(Math.toRadians(gyro.getPitch()))
        val y = total.y * cos(Math.toRadians(gyro.getRoll()))
        val z = total.x * sin(Math.toRadians(gyro.getPitch())) + total.y * sin(Math.toRadians(gyro.getRoll())) // This BS is to avoid another trig call (they are slow)
        return Vector3(x, y, z)
    }

    fun updatePosition() {
        fieldPosition += getVelocity() * (MiscCalculations.getCurrentTime() - lastTime)
        lastTime = MiscCalculations.getCurrentTime()
    }
}