package cshcyberhawks.swolib.hardware

import com.ctre.phoenix.sensors.Pigeon2
import cshcyberhawks.swolib.math.AngleCalculations

class Pigeon2Gyro(private val port: Int) : GenericGyro {
    val gyro = Pigeon2(port)
    var offsetValue: Double = 0.0

    /**
     * Gets the angle the gyro is currently facing.
     *
     * @return The current angle.
     */
    override fun getAngle(): Double {
        return AngleCalculations.wrapAroundAngles(gyro.yaw - offsetValue)  % 360
    }

    /**
     * Sets the angle offset of the gyro to the current direction.
     *
     * This exists because the built-in offset was refusing to work.
     */
    override fun setOffset() {
        offsetValue = AngleCalculations.wrapAroundAngles(gyro.yaw) % 360
    }


}