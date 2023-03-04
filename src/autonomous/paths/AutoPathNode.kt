package cshcyberhawks.swolib.autonomous.paths

data class AutoPathFieldPosition(
    val x: Double,
    val y: Double,
)

data class AutoPathNode(
    val point: AutoPathFieldPosition,
)