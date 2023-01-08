package cshcyberhawks.swolib.hardware.interfaces

import com.ctre.phoenix.motorcontrol.ControlMode

interface GenericDriveMotor {
    operator fun set(mode: ControlMode, value: Double)
    fun getVelocity(): Double
}