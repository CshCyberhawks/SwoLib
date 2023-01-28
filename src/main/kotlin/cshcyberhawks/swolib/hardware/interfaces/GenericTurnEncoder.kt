package cshcyberhawks.swolib.hardware.interfaces

interface GenericTurnEncoder {
    val port: Int

    /**
     * Gets the position of the encoder.
     *
     * @return The position in degrees.
     */
    fun get(): Double

    /**
     * Gets the raw position of the encoder (ignores the offset).
     *
     * @return The raw position in degrees.
     */
    fun getRaw(): Double
}