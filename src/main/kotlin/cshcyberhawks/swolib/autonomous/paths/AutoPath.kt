package cshcyberhawks.swolib.autonomous.paths

import com.beust.klaxon.Klaxon
import java.io.File

class AutoPath(inputFile: File) {
    val nodes: List<AutoPathNode> = Klaxon().parseArray<AutoPathNode>(inputFile)!!
}