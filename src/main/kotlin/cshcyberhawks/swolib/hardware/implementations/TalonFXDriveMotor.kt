package cshcyberhawks.swolib.hardware.implementations

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.can.TalonFX
import cshcyberhawks.swolib.hardware.enums.MotorNeutralMode
import cshcyberhawks.swolib.hardware.interfaces.GenericDriveMotor

class TalonFXDriveMotor(deviceId: Int, canBus: String = "") : GenericDriveMotor {
    private val motor = TalonFX(deviceId, canBus)
    override fun setInverted(inverted: Boolean) {
        motor.inverted = inverted
    }

    override fun setNeutralMode(mode: MotorNeutralMode) {
        val mode = when (mode) {
            MotorNeutralMode.Coast -> NeutralMode.Coast
            MotorNeutralMode.Brake -> NeutralMode.Brake
        }
        motor.setNeutralMode(mode)
    }

    override fun getVelocity(): Double = motor.selectedSensorVelocity / 204.8

    override fun setPercentOutput(percent: Double) {
        motor[ControlMode.PercentOutput] = percent
    }
}