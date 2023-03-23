package cshcyberhawks.swolib.hardware.interfaces

import cshcyberhawks.swolib.math.Vector2
import edu.wpi.first.math.geometry.Rotation2d

interface GenericGyro {
    /**
     * Gets the angle the gyro is currently facing.
     *
     * @return The current angle.
     */
    fun getYaw(): Double

    fun getPitch(): Double

    fun getRoll(): Double

    fun mergePitchRoll(): Vector2

    fun getYawRotation2d(): Rotation2d

    /**
     * Sets the angle offset of the gyro to the current direction.
     *
     * This exists because the built-in offset was refusing to work.
     */
    fun setYawOffset(currentPos: Double = 0.0)
}
