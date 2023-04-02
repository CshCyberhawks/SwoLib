package cshcyberhawks.swolib.hardware.implementations

import com.ctre.phoenix.sensors.Pigeon2
import cshcyberhawks.swolib.hardware.interfaces.GenericGyro
import cshcyberhawks.swolib.math.AngleCalculations
import cshcyberhawks.swolib.math.Polar
import cshcyberhawks.swolib.math.Vector2
import edu.wpi.first.math.geometry.Rotation2d

//document this constructor

/**
  * A gyro that uses the Pigeon2 IMU.
  * @param port The port the Pigeon2 is plugged into.
  * @param pitchOffset The offset of the pitch axis.
  * @param rollOffset The offset of the roll axis.
*/
class Pigeon2Gyro(port: Int, val pitchOffset: Double, val rollOffset: Double) : GenericGyro {
    val gyro = Pigeon2(port)
    private var offsetValue: Double = 0.0

    /**
     * Gets the angle the gyro is currently facing.
     *
     * @return The current angle.
     */
    override fun getYaw(): Double = AngleCalculations.wrapAroundAngles(gyro.yaw - offsetValue)
    override fun getPitch(): Double = gyro.pitch
    override fun getRoll(): Double = gyro.roll

    override fun mergePitchRoll(): Vector2 {
        return Vector2.fromPolar(Polar(pitchOffset, getPitch())) + Vector2.fromPolar(Polar(rollOffset, getRoll()))
    }

    /**
     * Sets the angle offset of the gyro to the current direction.
     *
     * This exists because the built-in offset was refusing to work.
     */
    override fun setYawOffset(currentPos: Double) {
        offsetValue = AngleCalculations.wrapAroundAngles(gyro.yaw - currentPos)
    }

    override fun getYawRotation2d(): Rotation2d {
        return Rotation2d(Math.toRadians(getYaw()))
    }


}
