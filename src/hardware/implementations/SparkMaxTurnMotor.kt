package cshcyberhawks.swolib.hardware.implementations

import com.ctre.phoenix.sensors.CANCoder
import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel
import cshcyberhawks.swolib.hardware.interfaces.GenericTurnMotor

class SparkMaxTurnMotor(deviceId: Int, encoderId: Int, val offset: Double, canBus: String = "") : GenericTurnMotor {
    val motor = CANSparkMax(deviceId, CANSparkMaxLowLevel.MotorType.kBrushless)
    val encoder = CANCoder(encoderId, canBus)
    override fun get(): Double = getRaw() - offset

    override fun getRaw(): Double = encoder.position / 4096

    override fun setPercentOutput(percent: Double) {
        motor.set(percent)
    }
}