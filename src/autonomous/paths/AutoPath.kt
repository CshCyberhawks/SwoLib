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
    val jsonData: AutoPathNode = Klaxon().parse<AutoPathNode>(inputFile)!!
    val positions = jsonData.positions.map {
        FieldPosition(Vector2(it.x, it.y), it.angle)
    }
    var currentCommand: GoToPosition? = null
    var currentIndex = 0

    init {
        swerveAuto.swo.fieldPosition = Vector3(jsonData.startPosition.x, jsonData.startPosition.y, 0.0)
        gyro.setYawOffset(jsonData.startPosition.angle)
    }

    override fun execute() {
        if ((currentCommand == null || currentCommand?.isFinished == true) && currentIndex < positions.size) {
            currentCommand = GoToPosition(swerveAuto, positions[currentIndex++])
            currentCommand?.schedule()
        }
    }

    override fun isFinished(): Boolean = currentIndex == positions.size
}