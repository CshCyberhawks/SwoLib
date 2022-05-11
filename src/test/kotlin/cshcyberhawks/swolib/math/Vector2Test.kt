package cshcyberhawks.swolib.math

import org.junit.Test

internal class Vector2Test {
    @Test
    fun testPlusAssign() {
        val a = Vector2(3, 4)
        val b = Vector2()
        val c = Vector2(3, 4)

        a += b
        a += c

        assert(a == Vector2(6, 8))
    }

    @Test
    fun testPlus() {
        val a = Vector2(3, 4)
        val b = Vector2(7, 6)

        assert(a + b == Vector2(10, 10))
    }

    @Test
    fun testMinusAssign() {
        val a = Vector2(10, 10)
        val b = Vector2(5, 7)

        a -= b

        assert(a == Vector2(5, 3))
    }

    @Test
    fun testMinus() {
        val a = Vector2(10, 10)
        val b = Vector2(7, 2)

        assert(a - b == Vector2(3, 8))
    }
}