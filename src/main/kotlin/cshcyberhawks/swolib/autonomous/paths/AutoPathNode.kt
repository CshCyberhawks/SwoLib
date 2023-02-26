package cshcyberhawks.swolib.autonomous.paths

data class AutoPathRotation(
    val radians: Double
)

data class AutoPathTranslation(
    val x: Double,
    val y: Double
)

data class AutoPathPose(
    val rotation: AutoPathRotation,
    val translation: AutoPathTranslation
)

data class AutoPathNode(
    val time: Double,
    val pose: AutoPathPose,
    val velocity: Double,
    val acceleration: Double,
    val curvature: Double,
    val holonomicRotation: Double,
    val angularVelocity: Double,
    val holonomicAngularVelocity: Double
)