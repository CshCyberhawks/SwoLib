package cshcyberhawks.swolib.autonomous.paths

import java.io.File
import com.beust.klaxon.Klaxon

class AutoPath(inputFile: File) {
    val nodes: List<AutoPathNode> = Klaxon().parseArray<AutoPathNode>(inputFile)!!
}