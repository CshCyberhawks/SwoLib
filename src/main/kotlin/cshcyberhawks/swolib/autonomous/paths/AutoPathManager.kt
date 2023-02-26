package cshcyberhawks.swolib.autonomous.paths

import cshcyberhawks.swolib.autonomous.SwerveAuto
import cshcyberhawks.swolib.hardware.interfaces.GenericGyro
import edu.wpi.first.wpilibj.Filesystem

class AutoPathManager(swerveAuto: SwerveAuto, gyro: GenericGyro) {
    val paths: HashMap<String, AutoPath> = hashMapOf()

    init {
        Filesystem.getDeployDirectory().resolve("./paths/").listFiles()?.forEach {
            paths[it.name.substring(0, it.name.indexOf("."))] = AutoPath(it, swerveAuto, gyro)
        }
    }
}