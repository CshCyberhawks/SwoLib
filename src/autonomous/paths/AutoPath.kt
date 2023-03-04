package cshcyberhawks.swolib.autonomous.paths

import com.beust.klaxon.Klaxon
import cshcyberhawks.swolib.autonomous.SwerveAuto
import cshcyberhawks.swolib.autonomous.commands.GoToPosition
import cshcyberhawks.swolib.hardware.interfaces.GenericGyro
import cshcyberhawks.swolib.limelight.AttachedCommandType
import cshcyberhawks.swolib.math.Vector2
import cshcyberhawks.swolib.math.Vector3
import edu.wpi.first.wpilibj2.command.CommandBase
import java.io.File

class AutoPath(
        inputFile: File,
        val swerveAuto: SwerveAuto,
        val gyro: GenericGyro,
        val commandsIn: HashMap<Int, Pair<CommandBase, AttachedCommandType>> = HashMap()
) : CommandBase() {
    var commandsToRun: List<CommandBase>

    val positions =
            Klaxon().parseArray<AutoPathNode>(inputFile)!!.map { Vector2(it.point.x, it.point.y) }

    var currentCommand: CommandBase? = null
    var currentIndex = 1

    init {
        commandsToRun =
                Klaxon().parseArray<AutoPathNode>(inputFile)!!.map {
                    GoToPosition(swerveAuto, Vector2(it.point.x, it.point.y))
                }

        swerveAuto.swo.fieldPosition = Vector3(positions[0].x, positions[0].y, 0.0)
        // if (commandsIn.size != 0) {
        //     commandsIn.forEach { (key, (cmd, typ)) ->
        //         if (key < commandsToRun.size) commandsToRun.add(cmd, key)
        //     }
        // }
        //        gyro.setYawOffset(jsonData.startPosition.angle)
    }

    override fun execute() {
        if ((currentCommand == null || currentCommand?.isFinished == true) &&
                        currentIndex < positions.size
        ) {
            if (commandsIn.containsKey(currentIndex + 1)) {
                val pair = commandsIn[currentIndex + 1]
                if (pair != null) {
                    val (cmd, typ) = pair

                    when (typ) {
                        AttachedCommandType.SYNC -> {
                            currentCommand = cmd
                            currentCommand?.schedule()
                        }
                        AttachedCommandType.ASYNC -> {
                            cmd.schedule()
                        }
                    }
                    return
                }
            }

            currentCommand = commandsToRun[currentIndex++]
            currentCommand?.schedule()
        }
    }

    override fun isFinished(): Boolean = currentIndex == commandsToRun.size
}
