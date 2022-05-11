package cshcyberhawks.swolib.math

class Vector2(x: Number = 0, y: Number = 0) {
    var x: Double = x.toDouble()
    var y: Double = y.toDouble()

    operator fun plusAssign(other: Vector2) {
        this.x += other.x
        this.y += other.y
    }

    operator fun plus(other: Vector2): Vector2 {
        return Vector2(this.x + other.x, this.y + other.y)
    }

    operator fun minusAssign(other: Vector2) {
        this.x -= other.x
        this.y -= other.y
    }

    operator fun minus(other: Vector2): Vector2 {
        return Vector2(this.x - other.x, this.y - other.y)
    }

    override operator fun equals(other: Any?): Boolean {
        if (other !is Vector2) return false
        return this.x == other.x && this.y == other.y
    }
}