package cshcyberhawks.swolib.hardware


import edu.wpi.first.wpilibj.AnalogInput

/**
 * A helper class for using analog encoders for turning motors in swerve drive.
 *
 * @constructor Creates the encoder.
 */
class AnalogTurnEncoder(val port: Int, private val offset: Double) {
    val encoder: AnalogInput = AnalogInput(port)

    private fun voltageToDegrees(input: Double): Double = input / (2.5 / 180)

    /**
     * Gets the position of the encoder.
     *
     * @return The position in degrees.
     */
    fun get(): Double = voltageToDegrees(encoder.voltage) - offset

    /**
     * Gets the raw position of the encoder (ignores the offset).
     *
     * @return The raw position in degrees.
     */
    fun getRaw(): Double = voltageToDegrees(encoder.voltage)
}


