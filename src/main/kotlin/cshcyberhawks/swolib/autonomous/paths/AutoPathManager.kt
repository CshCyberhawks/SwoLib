package cshcyberhawks.swolib.autonomous.paths

import edu.wpi.first.wpilibj.Filesystem

class AutoPathManager() {
    val paths: HashMap<String, AutoPath> = hashMapOf()

    init {
        Filesystem.getDeployDirectory().resolve("./paths/").listFiles()?.forEach {
            paths[it.name.substring(0, it.name.indexOf("."))] = AutoPath(it)
        }
    }
}