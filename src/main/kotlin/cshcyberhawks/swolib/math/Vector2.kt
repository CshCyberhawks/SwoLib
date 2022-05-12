package cshcyberhawks.swolib.math

/**
 * A class for managing a 2d vector/coordinate.
 *
 * @property x X value for the coordinate.
 * @property y Y value for the coordinate.
 *
 * @constructor Creates a vector2 with the specified x and y values.
 */
class Vector2(x: Number = 0, y: Number = 0) {
    var x: Double = x.toDouble()
    var y: Double = y.toDouble()

    /**
     * Override for the += operator.
     */
    operator fun plusAssign(other: Vector2) {
        this.x += other.x
        this.y += other.y
    }

    /**
     * Override for the + operator.
     */
    operator fun plus(other: Vector2): Vector2 {
        return Vector2(this.x + other.x, this.y + other.y)
    }

    /**
     * Override for the -= operator.
     */
    operator fun minusAssign(other: Vector2) {
        this.x -= other.x
        this.y -= other.y
    }

    /**
     * Override for the - operator.
     */
    operator fun minus(other: Vector2): Vector2 {
        return Vector2(this.x - other.x, this.y - other.y)
    }

    /**
     * Override for the == and != operators.
     */
    override operator fun equals(other: Any?): Boolean {
        if (other !is Vector2) return false
        return this.x == other.x && this.y == other.y
    }
}