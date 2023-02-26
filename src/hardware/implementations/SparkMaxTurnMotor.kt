package cshcyberhawks.swolib.hardware.implementations

import com.ctre.phoenix.sensors.CANCoder
import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel
import cshcyberhawks.swolib.hardware.interfaces.GenericTurnMotor
import cshcyberhawks.swolib.math.AngleCalculations

class SparkMaxTurnMotor(deviceId: Int, override val encoderPort: Int, val offset: Double, canBus: String = "") : GenericTurnMotor {
    val motor = CANSparkMax(deviceId, CANSparkMaxLowLevel.MotorType.kBrushless)
    val encoder = CANCoder(encoderPort, canBus)

    init {
        encoder.setPositionToAbsolute()
        encoder.configMagnetOffset(offset)
    }

    override fun get(): Double = AngleCalculations.wrapAroundAngles(encoder.position)

    override fun getRaw(): Double = AngleCalculations.wrapAroundAngles(encoder.position - offset)

    override fun setPercentOutput(percent: Double) {
        motor.set(percent)
    }
}