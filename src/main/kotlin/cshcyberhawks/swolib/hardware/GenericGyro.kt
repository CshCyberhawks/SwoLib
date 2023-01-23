package cshcyberhawks.swolib.hardware

interface GenericGyro {
    /**
     * Gets the angle the gyro is currently facing.
     *
     * @return The current angle.
     */
    fun getYaw(): Double

    fun getPitch(): Double

    fun getRoll(): Double

    /**
     * Sets the angle offset of the gyro to the current direction.
     *
     * This exists because the built-in offset was refusing to work.
     */
    fun setYawOffset()
}