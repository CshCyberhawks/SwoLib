package cshcyberhawks.swolib.math

/**
 * A class for managing a polar coordinate/vector.
 *
 * @property theta Theta or angle value for the vector.
 * @property r R value or distance of the vector.
 *
 * @constructor Creates a polar vector with the specified theta and r values.
 */
class Polar(theta: Number = 0, r: Number = 0) {
    var theta: Double = theta.toDouble()
    var r: Double = r.toDouble()

    /**
     * Override for the == and != operators.
     */
    override operator fun equals(other: Any?): Boolean {
        if (other !is Polar) return false
        return this.theta == other.theta && this.r == other.r
    }
}