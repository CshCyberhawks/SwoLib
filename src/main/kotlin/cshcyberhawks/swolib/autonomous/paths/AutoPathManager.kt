package cshcyberhawks.swolib.autonomous.paths

import edu.wpi.first.wpilibj.Filesystem
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard

class AutoPathManager() {
    val paths: HashMap<String, AutoPath> = hashMapOf()

    init {
        SmartDashboard.putNumber("Init", 1.0)
        Filesystem.getDeployDirectory().resolve("/paths/").listFiles()!!.forEach {
            SmartDashboard.putNumber(it.name, 1.0)
            paths[it.name] = AutoPath(it)
        }
    }
}