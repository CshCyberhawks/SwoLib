package cshcyberhawks.swolib.motors

import com.ctre.phoenix.motorcontrol.can.TalonFX

/**
 * A class for helping with built in TalonFX Drive Encoders.
 *
 * @property driveMotor The TalonFX you want the encoder for.
 */
class TalonFXDriveEncoder(var driveMotor: TalonFX) {
    /**
     * Gets the velocity of the encoder.
     *
     * @return The velocity in RPM.
     */
    fun getVelocity(): Number {
        return driveMotor.selectedSensorVelocity / 204.8
    }

    /**
     * Gets the position of the encoder.
     *
     * @return The position in rotations.
     */
    fun getPositon(): Number {
        return driveMotor.selectedSensorPosition / 2048
    }
}