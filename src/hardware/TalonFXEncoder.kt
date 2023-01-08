package cshcyberhawks.swolib.hardware

import com.ctre.phoenix.motorcontrol.can.TalonFX

/**
 * A class for helping with built in TalonFX Drive Encoders.
 *
 * @property driveMotor The TalonFX you want the encoder for.
 *
 * @constructor Gets the encoder from the TalonFX motor.
 */
class TalonFXEncoder(var driveMotor: TalonFX) {
    /**
     * Gets the velocity of the encoder.
     *
     * @return The velocity in RPM.
     */
    fun getVelocity(): Double = driveMotor.selectedSensorVelocity / 204.8

    /**
     * Gets the position of the encoder.
     *
     * @return The position in rotations.
     */
    fun getPosition(): Double = driveMotor.selectedSensorPosition / 2048
}