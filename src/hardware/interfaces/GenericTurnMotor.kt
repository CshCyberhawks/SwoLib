package cshcyberhawks.swolib.hardware.interfaces

import com.ctre.phoenix.motorcontrol.ControlMode

interface GenericTurnMotor {
    operator fun set(mode: ControlMode, value: Double)
    fun getTurnValue(): Double
}