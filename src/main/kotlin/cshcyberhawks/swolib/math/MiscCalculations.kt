package cshcyberhawks.swolib.math

import kotlin.math.abs

/**
 * Miscellaneous calculations.
 */
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
}