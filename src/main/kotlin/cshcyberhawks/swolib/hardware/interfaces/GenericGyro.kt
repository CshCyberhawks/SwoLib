package cshcyberhawks.swolib.hardware.interfaces

/**
 * A generic interface for a gyroscope that can be used with SwoLib
 */
interface GenericGyro {
    /**
     * Gets the angle the gyro is currently facing
     *
     * @return The current angle in range [0-360]
     */
    fun getAngle(): Double
}