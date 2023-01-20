package cshcyberhawks.swolib.math

class Vector3(var x: Double = 0.0, var y: Double = 0.0, var z: Double = 0.0) {
    operator fun plusAssign(other: Vector3) {
        x += other.x
        y += other.y
        z += other.z
    }

    operator fun times(other: Double): Vector3 = Vector3(x * other, y * other, z * other)
}