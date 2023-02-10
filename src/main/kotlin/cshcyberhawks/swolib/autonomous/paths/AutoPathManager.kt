package cshcyberhawks.swolib.autonomous.paths

import edu.wpi.first.wpilibj.Filesystem
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard

class AutoPathManager() {
    val paths: MutableList<AutoPath> = mutableListOf()

    init {
        Filesystem.getDeployDirectory().resolve("/paths").listFiles()?.forEach {
            paths.add(AutoPath(it))
        }
    }
}