package frc.robot.util

class Polar(var theta: Double = 0.0, var r: Double = 0.0) {
    fun add(other: Polar): Polar {
        return Polar(theta + other.theta, r + other.r)
    }

    fun equals(other: Polar): Boolean {
        return theta == other.theta && r == other.r
    }
}