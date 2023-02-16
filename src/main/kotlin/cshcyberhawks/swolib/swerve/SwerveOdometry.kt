package cshcyberhawks.swolib.swerve

import cshcyberhawks.swolib.hardware.interfaces.GenericGyro
import cshcyberhawks.swolib.limelight.Limelight
import cshcyberhawks.swolib.math.MiscCalculations
import cshcyberhawks.swolib.math.Polar
import cshcyberhawks.swolib.math.Vector2
import cshcyberhawks.swolib.math.Vector3
import kotlin.math.cos
import kotlin.math.sin

class SwerveOdometry(
    private var swerveDriveTrain: SwerveDriveTrain,
    private var gyro: GenericGyro,
    private val swoToMeters: Double,
    private val limelight: Limelight? = null
) {
    var fieldPosition = Vector3()
    var lastTime = MiscCalculations.getCurrentTime()

    fun getVelocity(): Vector3 {
        var total = Vector2()

        val wheelVectors = swerveDriveTrain.swerveConfiguration.getWheelVectors()
        for (wheel in wheelVectors) {
            val wheelVector = Vector2.fromPolar(wheel)
            total += wheelVector
        }
        total /= wheelVectors.size

        val polar = Polar.fromVector2(total)
        polar.theta -= gyro.getYaw()
        total = Vector2.fromPolar(polar)

        // Pitch and roll might be flipped
        val x = total.x * cos(Math.toRadians(gyro.getPitch())) / swoToMeters
        val y = total.y * cos(Math.toRadians(gyro.getRoll())) / swoToMeters
        val z = (total.x * sin(Math.toRadians(gyro.getPitch())) + total.y * sin(Math.toRadians(gyro.getRoll()))) / swoToMeters
        return Vector3(x, y, z)
    }

    fun updatePosition() {
        fieldPosition += getVelocity() * (MiscCalculations.getCurrentTime() - lastTime)

        if (limelight != null) {
            val limelightPosition = limelight.getBotPose()
            if (limelightPosition != null) {
                fieldPosition = limelightPosition
            }
        }

        lastTime = MiscCalculations.getCurrentTime()
    }
}
