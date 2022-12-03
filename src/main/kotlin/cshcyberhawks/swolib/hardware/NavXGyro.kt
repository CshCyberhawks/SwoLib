package cshcyberhawks.swolib.hardware

import com.kauailabs.navx.frc.AHRS
import cshcyberhawks.swolib.hardware.interfaces.GenericGyro
import cshcyberhawks.swolib.math.AngleCalculations
import edu.wpi.first.math.filter.LinearFilter
import edu.wpi.first.wpilibj.SPI

/**
 * A wrapper class for the NavX Gyroscope.
 *
 * @property port The port that your NavX is connected to.
 *
 * @constructor Creates a gyro with the specified port.
 */
class NavXGyro(private val port: SPI.Port): GenericGyro {
    val gyro: AHRS = AHRS(port)
    private val filter: LinearFilter = LinearFilter.highPass(0.1, 0.02)
    var offsetValue: Double = 0.0

    /**
     * Gets the angle the gyro is currently facing.
     *
     * @return The current angle.
     */
    override fun getAngle(): Double {
        return AngleCalculations.wrapAroundAngles(AngleCalculations.wrapAroundAngles(gyro.yaw.toDouble()) - offsetValue)
    }

    /**
     * Finds if the gyro is connected.
     *
     * @return A boolean that is true when connected and false when not.
     */
    fun isConnected(): Boolean {
        return gyro.isConnected
    }

    /**
     * Resets the gyro yaw.
     */
    fun reset() {
        gyro.reset()
    }

    /**
     * Sets the angle offset of the gyro to the current direction.
     *
     * This exists because the built-in offset was refusing to work.
     */
    fun setOffset() {
        offsetValue = AngleCalculations.wrapAroundAngles(gyro.yaw.toDouble())
    }

    /**
     * Gets the current x velocity.
     *
     * @return The current x velocity.
     */
    fun getVelocityX(): Double {
        return filter.calculate(gyro.velocityX.toDouble())
    }

    /**
     * Gets the current y velocity.
     *
     * @return The current y velocity.
     */
    fun getVelocityY(): Double {
        return filter.calculate(gyro.velocityY.toDouble())
    }

    /**
     * Gets the current z velocity.
     *
     * @return The current z velocity.
     */
    fun getVelocityZ(): Double {
        return filter.calculate(gyro.velocityZ.toDouble())
    }

    /**
     * Gets the current x acceleration.
     *
     * @return The current x acceleration.
     */
    fun getAccelerationX(): Double {
        return filter.calculate(gyro.worldLinearAccelX.toDouble())
    }

    /**
     * Gets the current x acceleration.
     *
     * @return The current x acceleration.
     */
    fun getAccelerationY(): Double {
        return filter.calculate(gyro.worldLinearAccelX.toDouble())
    }

    /**
     * Calibrates the gyro.
     */
    fun calibrate() {
        gyro.calibrate()
    }
}