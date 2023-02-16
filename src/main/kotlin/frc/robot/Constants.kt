package frc.robot

object Constants {
    // TalonSRX Motors
    const val frontRightTurnMotor: Int = 8
    const val frontLeftTurnMotor: Int = 7
    const val backRightTurnMotor: Int = 9
    const val backLeftTurnMotor: Int = 6

    // Falcon Motors
    const val frontRightDriveMotor: Int = 4
    const val frontLeftDriveMotor: Int = 3
    const val backRightDriveMotor: Int = 5
    const val backLeftDriveMotor: Int = 2

    // Encoders
    const val frontRightEncoder = 12
    const val frontLeftEncoder = 11
    const val backRightEncoder = 13
    const val backLeftEncoder = 10

    val turnEncoderOffsets: Array<Double> = arrayOf(0.0, 0.0, 0.0, 0.0)
}
