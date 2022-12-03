package cshcyberhawks.swolib.hardware

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.can.TalonFX
import cshcyberhawks.swolib.hardware.interfaces.GenericDriveMotor

class FalconDriveMotor(private val motor: TalonFX) : GenericDriveMotor {
    private val encoder = TalonFXDriveEncoder(motor)

    init {
        motor.setNeutralMode(NeutralMode.Brake)
    }

    override fun set(mode: ControlMode, value: Double) {
        motor[mode] = value
    }

    override fun getVelocity(): Double {
        return encoder.getVelocity()
    }
}