package cshcyberhawks.swolib.math

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Vector2Test {
    @Test
    fun distance() {
        var a = Vector2(0.0, 0.0)
        var b = Vector2(3.0, 4.0)
        assertEquals(5.0, a.distance(b))

        a = Vector2(0.0, 0.0)
        b = Vector2(0.0, 0.0)
        assertEquals(0.0, a.distance(b))
    }
}
