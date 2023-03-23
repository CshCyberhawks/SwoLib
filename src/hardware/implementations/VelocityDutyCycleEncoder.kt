package cshcyberhawks.swolib.hardware.implementations

import cshcyberhawks.swolib.math.MiscCalculations
import edu.wpi.first.wpilibj.DutyCycleEncoder

class VelocityDutyCycleEncoder(port: Int) {
    private val encoder = DutyCycleEncoder(port)
    private var lastTime = MiscCalculations.getCurrentTime()
    private var lastPosition = encoder.get()

    val velocity: Double
        get() {
            val currentPosition = encoder.get()
            val currentTime = MiscCalculations.getCurrentTime()

            val velo = (currentPosition - lastPosition) / (currentTime - lastTime)

            lastPosition = currentPosition
            lastTime = currentTime

            return velo
        }

    val absolutePosition
        get() = encoder.absolutePosition

    var positionOffset
        get() = encoder.positionOffset
        set(value) {
            encoder.positionOffset = value
        }

    fun get(): Double {
        return encoder.get()
    }
}