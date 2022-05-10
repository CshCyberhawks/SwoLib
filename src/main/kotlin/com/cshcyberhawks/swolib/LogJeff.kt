package com.cshcyberhawks.swolib
import edu.wpi.first.math.controller.PIDController;



object LogJeff {
    private var controller: PIDController = PIDController(0.1, 0.0, 0.0)
    fun log(value: Double) {
        println(controller.calculate(value, 1.0))
    }
}
