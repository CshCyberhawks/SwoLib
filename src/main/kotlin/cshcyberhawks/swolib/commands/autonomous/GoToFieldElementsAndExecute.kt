package cshcyberhawks.swolib.commands.autonomous

import cshcyberhawks.swolib.autonomous.FieldElement
import cshcyberhawks.swolib.autonomous.SwerveAuto
import edu.wpi.first.wpilibj2.command.CommandBase

class GoToFieldElementsAndExecute(
        val swerveAuto: SwerveAuto,
        val fieldElements: List<FieldElement>,
        val commands: List<CommandBase>
) : CommandBase() {
    constructor(
            swerveAuto: SwerveAuto,
            elements: List<FieldElement>,
            commands: List<CommandBase>,
            finishCondition: List<GoToPositionAndExecute.FinishCondition>) : this(swerveAuto, elements.toList(), commands) {
                this.finishCondition = finishCondition
            }

    private var finishCondition: List<GoToPositionAndExecute.FinishCondition> = listOf(GoToPositionAndExecute.FinishCondition.BOTH)

    private lateinit var command: GoToPositionAndExecute

    override fun initialize() {
        command = GoToPositionAndExecute(swerveAuto, fieldElements.first().position, commands.first(), finishCondition.first())
        commands.drop(1)
        fieldElements.drop(1)
        finishCondition.drop(1)
    }

    private fun isPieceDone(cond: GoToPositionAndExecute.FinishCondition): Boolean {
        when (cond) {
            GoToPositionAndExecute.FinishCondition.POSITION -> return swerveAuto.isFinishedMoving()
            GoToPositionAndExecute.FinishCondition.COMMAND -> return command.isFinished
            GoToPositionAndExecute.FinishCondition.BOTH -> return swerveAuto.isFinishedMoving() && command.isFinished()
            GoToPositionAndExecute.FinishCondition.EITHER -> return swerveAuto.isFinishedMoving() || command.isFinished()
        }

    }

    override fun execute() {
        if (isPieceDone(finishCondition.first())) {
            if (fieldElements.isNotEmpty()) {
                command = GoToPositionAndExecute(swerveAuto, fieldElements.first().position, commands.first(), finishCondition.first())
                commands.drop(1)
                fieldElements.drop(1)
                finishCondition.drop(1)
                command.schedule();
            }
        } 
    }

    override fun isFinished(): Boolean {
        return fieldElements.isEmpty()
    }
}
