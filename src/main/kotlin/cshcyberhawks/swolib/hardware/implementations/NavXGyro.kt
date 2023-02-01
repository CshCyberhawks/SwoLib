package cshcyberhawks.swolib.hardware.implementations

import com.kauailabs.navx.frc.AHRS
import cshcyberhawks.swolib.hardware.interfaces.GenericGyro
import cshcyberhawks.swolib.math.AngleCalculations
import edu.wpi.first.wpilibj.SPI

/**
 * A wrapper class for the NavX Gyroscope.
 *
 * @property port The port that your NavX is connected to.
 *
 * @constructor Creates a gyro with the specified port.
 */
class NavXGyro(private val port: SPI.Port) : GenericGyro {
    val gyro: AHRS = AHRS(port)
    var offsetValue: Double = 0.0

    /**
     * Gets the angle the gyro is currently facing.
     *
     * @return The current angle.
     */
    override fun getYaw(): Double = AngleCalculations.wrapAroundAngles(gyro.yaw.toDouble() - offsetValue)
    override fun getPitch(): Double = gyro.pitch.toDouble()

    override fun getRoll(): Double = gyro.roll.toDouble()

    /**
     * Sets the angle offset of the gyro to the current direction.
     *
     * This exists because the built-in offset was refusing to work.
     */
    override fun setYawOffset() {
        offsetValue = AngleCalculations.wrapAroundAngles(gyro.yaw.toDouble())
    }
}