package cshcyberhawks.swolib.math

class Vector2 {
    var x: Double = 0.0
    var y: Double = 0.0

    constructor(x: Number, y: Number) {
        this.x = x.toDouble()
        this.y = y.toDouble()
    }

    operator fun plusAssign(other: Vector2) {
        this.x += other.x
        this.y += other.y
    }

    operator fun plus(other: Vector2): Vector2 {
        return Vector2(this.x + other.x, this.y + other.y)
    }
}
