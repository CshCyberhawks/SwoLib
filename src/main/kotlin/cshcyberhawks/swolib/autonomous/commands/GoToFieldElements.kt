package cshcyberhawks.swolib.autonomous.commands

import cshcyberhawks.swolib.autonomous.FieldElement
import cshcyberhawks.swolib.autonomous.SwerveAuto
import edu.wpi.first.wpilibj2.command.CommandBase

class GoToFieldElements(val swerveAuto: SwerveAuto, private val fieldElements: List<FieldElement>) :
    CommandBase() {
    private lateinit var posCommand: GoToPosition

    override fun initialize() {
        posCommand = GoToPosition(swerveAuto, fieldElements.first().position)
    }

    override fun execute() {
        if (posCommand.isFinished && fieldElements.isNotEmpty()) {
            val next = fieldElements.first()
            posCommand = GoToPosition(swerveAuto, next.position)
            fieldElements.drop(1)
            posCommand.schedule()
        }
    }

    override fun isFinished(): Boolean {
        return fieldElements.isEmpty()
    }
}
