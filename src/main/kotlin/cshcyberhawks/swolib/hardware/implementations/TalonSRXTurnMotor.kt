package cshcyberhawks.swolib.hardware.implementations

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import cshcyberhawks.swolib.hardware.interfaces.GenericTurnMotor
import edu.wpi.first.wpilibj.AnalogInput

class TalonSRXTurnMotor(val deviceId: Int, override val encoderPort: Int, private val offset: Double) : GenericTurnMotor {
    private val motor = TalonSRX(deviceId)
    private val encoder: AnalogInput = AnalogInput(encoderPort)

    private fun voltageToDegrees(input: Double): Double = input / (2.5 / 180)

    override fun get(): Double = getRaw() - offset

    override fun getRaw(): Double = voltageToDegrees(encoder.voltage)

    override fun setPercentOutput(percent: Double) {
        motor[ControlMode.PercentOutput] = percent
    }
}