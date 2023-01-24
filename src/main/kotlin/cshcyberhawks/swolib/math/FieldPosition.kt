package cshcyberhawks.swolib.math

import kotlin.math.cos
import kotlin.math.sin

class FieldPosition(var position: Vector2, var angle: Double) {

    constructor(x: Double, y: Double, angle: Double) : this(Vector2(x, y), angle)

    var x
        get() = position.x
        set(value) {
            position.x = value
        }
    var y
        get() = position.y
        set(value) {
            position.y = value
        }

    override fun equals(other: Any?): Boolean {
        return position == other && angle == other
    }
}
