package cshcyberhawks.swolib.math

/**
 * A collection of methods for calculations related to our invented unit the "swo".
 */
object SwoConversions {
    /**
     * Converts from swos to meters.
     *
     * @param swos The number of swos traveled.
     *
     * @return The number of meters traveled.
     */
    fun swosToMeters(swos: Number): Number {
        return swos.toDouble() * 0.27083333333
    }

    /**
     * Converts from meters to swos.
     *
     * @param meters The number of meters traveled.
     *
     * @return The number of swos traveled.
     */
    fun metersToSwos(meters: Number): Number {
        return meters.toDouble() * 3.69230769231
    }
}