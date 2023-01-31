package cshcyberhawks.swolib.hardware

/**
 * A collection of functions which can be used as a template for another gyro class
 */
interface GenericGyro {
    /**
     * Gets the angle the gyro is currently facing.
     *
     * @return The current angle.
     */
    fun getAngle(): Double

    /**
     * Sets the angle offset of the gyro to the current direction.
     *
     * This exists because the built-in offset was refusing to work.
     */
    fun setOffset()
}