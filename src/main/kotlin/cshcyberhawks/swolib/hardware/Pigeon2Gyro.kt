package cshcyberhawks.swolib.hardware

import com.ctre.phoenix.sensors.Pigeon2
import cshcyberhawks.swolib.hardware.interfaces.GenericGyro
import cshcyberhawks.swolib.math.AngleCalculations

class Pigeon2Gyro(private val port: Int) : GenericGyro {
    val gyro = Pigeon2(port)
    var offsetValue: Double = 0.0

    /**
     * Gets the angle the gyro is currently facing.
     *
     * @return The current angle.
     */
    override fun getYaw(): Double = AngleCalculations.wrapAroundAngles(gyro.yaw - offsetValue)
    override fun getPitch(): Double = gyro.pitch
    override fun getRoll(): Double = gyro.roll

    /**
     * Sets the angle offset of the gyro to the current direction.
     *
     * This exists because the built-in offset was refusing to work.
     */
    override fun setYawOffset() {
        offsetValue = AngleCalculations.wrapAroundAngles(gyro.yaw)
    }


}