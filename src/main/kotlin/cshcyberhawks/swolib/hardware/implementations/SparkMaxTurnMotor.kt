package cshcyberhawks.swolib.hardware.implementations

import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel
import cshcyberhawks.swolib.hardware.interfaces.GenericTurnMotor

class SparkMaxTurnMotor(val deviceId: Int, val offset: Double) : GenericTurnMotor {
    val motor = CANSparkMax(deviceId, CANSparkMaxLowLevel.MotorType.kBrushless)
    val encoder = motor.encoder
    override fun get(): Double = getRaw() - offset

    override fun getRaw(): Double = encoder.position

    override fun setPercentOutput(percent: Double) {
        motor.set(percent)
    }
}