package cshcyberhawks.swolib.autonomous.commands

import cshcyberhawks.swolib.autonomous.FieldElement
import cshcyberhawks.swolib.autonomous.FinishCondition
import cshcyberhawks.swolib.autonomous.SwerveAuto
import edu.wpi.first.wpilibj2.command.CommandBase

class GoToFieldElementsAndExecute(
    val swerveAuto: SwerveAuto,
    private val fieldElements: List<FieldElement>,
    val commands: List<CommandBase>,
    private val finishCondition: List<FinishCondition> = listOf(FinishCondition.BOTH)
) : CommandBase() {
    private lateinit var command: GoToPositionAndExecute

    override fun initialize() {
        command = GoToPositionAndExecute(
            swerveAuto,
            fieldElements.first().position,
            commands.first(),
            finishCondition.first()
        )
        commands.drop(1)
        fieldElements.drop(1)
        finishCondition.drop(1)
    }

    private fun isPieceDone(cond: FinishCondition): Boolean {
        return when (cond) {
            FinishCondition.POSITION -> swerveAuto.isFinishedMoving()
            FinishCondition.COMMAND -> command.isFinished
            FinishCondition.BOTH -> swerveAuto.isFinishedMoving() && command.isFinished
            FinishCondition.EITHER -> swerveAuto.isFinishedMoving() || command.isFinished
        }

    }

    override fun execute() {
        if (isPieceDone(finishCondition.first()) && fieldElements.isNotEmpty()) {
            command = GoToPositionAndExecute(
                swerveAuto,
                fieldElements.first().position,
                commands.first(),
                finishCondition.first()
            )
            commands.drop(1)
            fieldElements.drop(1)
            finishCondition.drop(1)
            command.schedule()
        }
    }

    override fun isFinished(): Boolean {
        return fieldElements.isEmpty()
    }
}
