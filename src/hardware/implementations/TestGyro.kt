package cshcyberhawks.swolib.hardware.implementations

import cshcyberhawks.swolib.hardware.interfaces.GenericGyro
import cshcyberhawks.swolib.math.Vector2

class TestGyro(private val retYaw: Double = 0.0, private val retPitch: Double = 0.0, private val retRoll: Double = 0.0) : GenericGyro {
    override fun getYaw(): Double = retYaw

    override fun getPitch(): Double = retPitch

    override fun getRoll(): Double = retRoll

    override fun mergePitchRoll(): Vector2 = Vector2()

    override fun setYawOffset(currentPos: Double) {}
}