package cshcyberhawks.swolib.autonomous.paths

import java.io.File
import com.beust.klaxon.Klaxon
import cshcyberhawks.swolib.autonomous.SwerveAuto
import cshcyberhawks.swolib.autonomous.commands.GoToPosition
import cshcyberhawks.swolib.math.FieldPosition
import cshcyberhawks.swolib.math.Vector2
import edu.wpi.first.wpilibj2.command.CommandBase

class AutoPath(inputFile: File, val swerveAuto: SwerveAuto) : CommandBase() {
    val positions: List<FieldPosition> = Klaxon().parseArray<AutoPathNode>(inputFile)!!.map {
        FieldPosition(Vector2(it.x, it.y), it.rotation)
    }
    var currentCommand: GoToPosition? = null
    var currentIndex = 0

    override fun execute() {
        if ((currentCommand == null || currentCommand?.isFinished == true) && currentIndex < positions.size) {
            currentCommand = GoToPosition(swerveAuto, positions[currentIndex++])
            currentCommand?.schedule()
        }
    }

    override fun isFinished(): Boolean = currentIndex == positions.size
}