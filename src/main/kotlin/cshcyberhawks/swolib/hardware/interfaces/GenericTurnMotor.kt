package cshcyberhawks.swolib.hardware.interfaces

interface GenericTurnMotor : GenericMotor {
    fun get(): Double
    fun getRaw(): Double
}