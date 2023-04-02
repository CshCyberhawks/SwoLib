package cshcyberhawks.swolib.math

/***
 * A class to keep track of position and rotation on the field
 *
 * @param position The position on the field in meters
 * @param angle The angle on the field
 */
class FieldPosition(private var position: Vector2 = Vector2(), var angle: Double = 0.0) {
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

    val angleRadians
        get() = Math.toRadians(angle)
    override fun equals(other: Any?): Boolean {
        return position == other && angle == other
    }

    override fun hashCode(): Int {
        var result = position.hashCode()
        result = 31 * result + angle.hashCode()
        return result
    }
}
