package cshcyberhawks.swolib.hardware

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import cshcyberhawks.swolib.hardware.interfaces.GenericTurnMotor

class TalonSRXTurnMotor(motorPort: Int, private val encoder: AnalogTurnEncoder) : GenericTurnMotor {
    private val motor = TalonSRX(motorPort)

    override operator fun set(mode: ControlMode, value: Double) {
        motor[mode] = value
    }

    override fun getTurnValue(): Double {
        return encoder.get()
    }
}