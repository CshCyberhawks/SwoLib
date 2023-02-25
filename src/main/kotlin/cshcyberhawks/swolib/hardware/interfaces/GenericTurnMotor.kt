package cshcyberhawks.swolib.hardware.interfaces

interface GenericTurnMotor : GenericMotor {
    val encoderPort: Int
    fun get(): Double
    fun getRaw(): Double
}