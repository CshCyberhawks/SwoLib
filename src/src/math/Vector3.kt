package cshcyberhawks.swolib.math

/**
 * A class to represent a 3D coordinate
 *
 * @param x The value on the x-axis
 * @param y The value on the y-axis
 * @param z The value on the z-axis
 */
class Vector3(var x: Double = 0.0, var y: Double = 0.0, var z: Double = 0.0) {
    operator fun plus(other: Vector3): Vector3 = Vector3(x + other.x, y + other.y, z + other.z)

    operator fun times(other: Double): Vector3 = Vector3(x * other, y * other, z * other)
}
