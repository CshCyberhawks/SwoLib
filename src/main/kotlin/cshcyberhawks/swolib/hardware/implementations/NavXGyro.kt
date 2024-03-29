package cshcyberhawks.swolib.hardware.implementations

import com.kauailabs.navx.frc.AHRS
import cshcyberhawks.swolib.hardware.interfaces.GenericGyro
import cshcyberhawks.swolib.math.AngleCalculations
import cshcyberhawks.swolib.math.Polar
import cshcyberhawks.swolib.math.Vector2
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.wpilibj.SPI

/**
 * A wrapper class for the NavX Gyroscope.
 *
 * @property port The port that your NavX is connected to.
 * @property pitchOffset The offset of the pitch axis.
 * @property rollOffset The offset of the roll axis.
 *
 * @constructor Creates a gyro with the specified port.
 */
class NavXGyro(private val port: SPI.Port, val pitchOffset: Double, val rollOffset: Double) : GenericGyro {
    val gyro: AHRS = AHRS(port)
    private var offsetValue: Double = 0.0

    /**
     * Gets the angle the gyro is currently facing.
     *
     * @return The current angle.
     */
    override fun getYaw(): Double = -AngleCalculations.wrapAroundAngles(gyro.yaw.toDouble() - offsetValue)
    override fun getPitch(): Double = gyro.pitch.toDouble()

    override fun getRoll(): Double = gyro.roll.toDouble()

    override fun mergePitchRoll(): Vector2 {
        return Vector2.fromPolar(Polar(pitchOffset, getPitch())) + Vector2.fromPolar(Polar(rollOffset, getRoll()))
    }


    /**
     * Sets the angle offset of the gyro to the current direction.
     *
     * This exists because the built-in offset was refusing to work.
     */
    override fun setYawOffset(currentPos: Double) {
        offsetValue = AngleCalculations.wrapAroundAngles(gyro.yaw.toDouble() - currentPos)
    }

    override fun getYawRotation2d(): Rotation2d {
        return gyro.rotation2d
    }
}
