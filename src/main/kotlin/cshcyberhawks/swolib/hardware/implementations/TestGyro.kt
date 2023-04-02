package cshcyberhawks.swolib.hardware.implementations

import cshcyberhawks.swolib.hardware.interfaces.GenericGyro
import cshcyberhawks.swolib.math.Vector2
import edu.wpi.first.math.geometry.Rotation2d

class TestGyro(
    private val retYaw: Double = 0.0,
    private val retPitch: Double = 0.0,
    private val retRoll: Double = 0.0
) : GenericGyro {
    override fun getYaw(): Double = retYaw

    override fun getPitch(): Double = retPitch

    override fun getRoll(): Double = retRoll

    override fun mergePitchRoll(): Vector2 = Vector2()

    override fun setYawOffset(currentPos: Double) {}

    override fun getYawRotation2d(): Rotation2d = Rotation2d(Math.toRadians(retYaw))
}