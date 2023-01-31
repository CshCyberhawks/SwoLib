package cshcyberhawks.swolib.hardware


import edu.wpi.first.wpilibj.AnalogInput

/**
 * A helper class for using analog encoders for turning motors in swerve drive.
 *
 * @constructor Creates the encoder.
 */
class AnalogTurnEncoder(override val port: Int, private val offset: Double) : GenericTurnEncoder {
    val encoder: AnalogInput = AnalogInput(port)

    /**
     * Converts a value in volts to a position in degrees
     *
     * @param input The voltage
     *
     * @return A position in degrees
     */
    private fun voltageToDegrees(input: Double): Double = input / (2.5 / 180)

    /**
     * Gets the position of the encoder.
     *
     * @return The position in degrees.
     */
    override fun get(): Double = voltageToDegrees(encoder.voltage) - offset

    /**
     * Gets the raw position of the encoder (ignores the offset).
     *
     * @return The raw position in degrees.
     */
    override fun getRaw(): Double = voltageToDegrees(encoder.voltage)
}


