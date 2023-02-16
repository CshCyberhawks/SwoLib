package cshcyberhawks.swolib.math

/***
 * A class to keep track of position and rotation on the field
 *
 * @param position The position on the field in meters
 * @param angle The angle on the field
 */
class FieldPosition(private var position: Vector2, var angle: Double) {
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
