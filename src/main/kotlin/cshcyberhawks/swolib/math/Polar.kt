package cshcyberhawks.swolib.math

class Polar(theta: Number = 0, r: Number = 0) {
    var theta: Double = theta.toDouble()
    var r: Double = r.toDouble()

    override operator fun equals(other: Any?): Boolean {
        if (other !is Polar) return false
        return this.theta == other.theta && this.r == other.r
    }
}