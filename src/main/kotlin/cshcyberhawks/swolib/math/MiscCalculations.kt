package cshcyberhawks.swolib.math

import kotlin.math.abs

/** Miscellaneous calculations. */
object MiscCalculations {
    /**
     * Converts Gs to meters per second.
     *
     * @param g The number of Gs you are traveling.
     *
     * @return The speed in meters per second.
     */
    fun gToMetersPerSecond(g: Number): Number {
        return g.toDouble() / 9.8066
    }

    /**
     * Deadzones an input so it can not be below a certain value.
     *
     * @param input The input you want to deadzone.
     * @param deadzoneValue The minimum value you will allow.
     *
     * @return The deadzoned value.
     */
    fun calculateDeadzone(input: Number, deadzoneValue: Number): Number {
        return if (abs(input.toDouble()) > deadzoneValue.toDouble()) input else 0
    }

    private fun getMinMax(values: DoubleArray): DoubleArray {
        var min = values[0]
        var max = values[0]
        for (v in values) {
            if (v < min) min = v
            if (v > max) max = v
        }
        return doubleArrayOf(min, max)
    }

    /**
     * Normalizes wheel speeds around their minimum and maximum values
     *
     * @param speeds An array of doubles of the speeds to be normalized
     *
     * @param maxSpeed The maximum value of which to normalize around @param minSpeed The minimum
     * value to normalize around
     *
     * @return A double array containing the normalized speeds
     */
    fun normalizeSpeeds(speeds: DoubleArray, maxSpeed: Double, minSpeed: Double): DoubleArray {
        val minMax = getMinMax(speeds)
        val divSpeed = if (Math.abs(minMax[0]) > minMax[1]) Math.abs(minMax[0]) else minMax[1]
        val highestSpeed = if (minMax[1] > maxSpeed) maxSpeed else minMax[1]
        val lowestSpeed = if (minMax[0] < minSpeed) minSpeed else minMax[0]
        for (i in speeds.indices) {
            if (minMax[1] > maxSpeed && speeds[i] > 0)
                    speeds[i] = speeds[i] / divSpeed * highestSpeed
            else if (minMax[0] < minSpeed && speeds[i] < 0)
                    speeds[i] = speeds[i] / -divSpeed * lowestSpeed
        }
        return speeds
    }
}
