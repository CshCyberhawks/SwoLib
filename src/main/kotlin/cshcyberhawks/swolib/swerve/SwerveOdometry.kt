package cshcyberhawks.swolib.swerve

import cshcyberhawks.swolib.hardware.GenericGyro
import cshcyberhawks.swolib.math.Coordinate
import cshcyberhawks.swolib.math.MiscCalculations
import cshcyberhawks.swolib.math.Vector3
import edu.wpi.first.wpilibj2.command.SubsystemBase
import kotlin.math.cos
import kotlin.math.sqrt

class SwerveOdometry(private val swerveDriveTrain: SwerveDriveTrain, private val gyro: GenericGyro): SubsystemBase() {
    var fieldPosition: Vector3 = Vector3()

    private var lastUpdateTime: Double = MiscCalculations.getCurrentTime()

    fun getVelocity(wheelVectors: Array<Coordinate> = swerveDriveTrain.swerveConfiguration.getWheelVectors()): Vector3 {
        var total = Coordinate()

        wheelVectors.forEach { total += it }

        total /= wheelVectors.size

        val returnCoordinate = Vector3()
        returnCoordinate.x = total.x * cos(gyro.getPitch()) // Pitch and roll may be swapped if gyro is oriented weirdly
        returnCoordinate.y = total.y * cos(gyro.getRoll())

        // Pythagorean Theorem because computers can't do trig (quickly)
        returnCoordinate.z = sqrt(total.x * total.x - returnCoordinate.x * returnCoordinate.x) + sqrt(total.y * total.y - returnCoordinate.y * returnCoordinate.y)

        return returnCoordinate
    }

    override fun periodic() {
        fieldPosition += getVelocity() * (MiscCalculations.getCurrentTime() - lastUpdateTime)
        lastUpdateTime = MiscCalculations.getCurrentTime()
    }
}
