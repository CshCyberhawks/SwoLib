package cshcyberhawks.swolib.autonomous.paths

import java.io.File
import com.beust.klaxon.Klaxon
import cshcyberhawks.swolib.autonomous.SwerveAuto
import cshcyberhawks.swolib.autonomous.commands.GoToPosition
import cshcyberhawks.swolib.hardware.interfaces.GenericGyro
import cshcyberhawks.swolib.math.FieldPosition
import cshcyberhawks.swolib.math.Vector2
import cshcyberhawks.swolib.math.Vector3
import edu.wpi.first.wpilibj2.command.CommandBase

class AutoPath(inputFile: File, val swerveAuto: SwerveAuto, val gyro: GenericGyro) : CommandBase() {
    val positions: List<Vector2> = Klaxon().parseArray<AutoPathNode>(inputFile)!!.map { Vector2(it.point.x, it.point.y) }
    var currentCommand: GoToPosition? = null
    var currentIndex = 1

    init {
        swerveAuto.swo.fieldPosition = Vector3(positions[0].x, positions[0].y, 0.0)
//        gyro.setYawOffset(jsonData.startPosition.angle)
    }

    override fun execute() {
        if ((currentCommand == null || currentCommand?.isFinished == true) && currentIndex < positions.size) {
            currentCommand = GoToPosition(swerveAuto, positions[currentIndex++])
            currentCommand?.schedule()
        }
    }

    override fun isFinished(): Boolean = currentIndex == positions.size
}