package frc.robot

object Constants {
    // TalonSRX Motors
    const val frontRightTurnMotor: Int = 1
    const val frontLeftTurnMotor: Int = 3
    const val backRightTurnMotor: Int = 5
    const val backLeftTurnMotor: Int = 2

    // Falcon Motors
    const val frontRightDriveMotor: Int = 9
    const val frontLeftDriveMotor: Int = 7
    const val backRightDriveMotor: Int = 6
    const val backLeftDriveMotor: Int = 8

    // Encoders
    const val frontRightEncoder = 0
    const val frontLeftEncoder = 1
    const val backRightEncoder = 3
    const val backLeftEncoder = 2

    val turnEncoderOffsets: Array<Double> = arrayOf(260.507785824, 312.53903049600007, 291.884735736, 102.041005176)
}