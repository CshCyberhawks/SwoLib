package cshcyberhawks.swolib.hardware.interfaces

import cshcyberhawks.swolib.hardware.enums.MotorNeutralMode

interface GenericDriveMotor : GenericMotor {
    fun setInverted(inverted: Boolean)
    fun setNeutralMode(mode: MotorNeutralMode)
    fun getVelocity(): Double
}