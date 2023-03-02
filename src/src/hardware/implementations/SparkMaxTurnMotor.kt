package cshcyberhawks.swolib.hardware.implementations

import com.ctre.phoenix.sensors.CANCoder
import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel
import cshcyberhawks.swolib.hardware.interfaces.GenericTurnMotor
import cshcyberhawks.swolib.math.AngleCalculations

class SparkMaxTurnMotor(deviceId: Int, override val encoderPort: Int, val offset: Double, canBus: String = "") : GenericTurnMotor {
    val motor = CANSparkMax(deviceId, CANSparkMaxLowLevel.MotorType.kBrushless)
    val encoder = CANCoder(encoderPort, canBus)

    override fun get(): Double = AngleCalculations.wrapAroundAngles(encoder.absolutePosition - offset)

    override fun getRaw(): Double = AngleCalculations.wrapAroundAngles(encoder.absolutePosition)

    override fun setPercentOutput(percent: Double) {
        motor.set(percent)
    }
}