package cshcyberhawks.swolib.math

object SwoConversions {
    fun swosToMeters(swos: Number): Number {
        return swos.toDouble() * 0.27083333333
    }

    fun metersToSwos(meters: Number): Number {
        return meters.toDouble() * 3.69230769231
    }
}