package cshcyberhawks.swolib.hardware

import edu.wpi.first.networktables.NetworkTableInstance
import kotlin.math.tan

class Limelight {
    private val limelight = NetworkTableInstance.getDefault().getTable("limelight")

    /**
     * A class for helping with built in TalonFX Drive Encoders.
     *
     * @property ledMode Sets limelight's current state: 0 = use the LED mode set in the current pipeline, 1 =
     * force off, 2 = force blink, 3 = force on.
     *
     * @property camMode Sets limelight's operation mode: 0 = Vision processor, 1 = Driver Camera (Increases exposure,
     * disables vision processing).
     *
     * @property pipeline Sets limelight's current pipeline: 0..9
     *
     * @property stream Sets limelight's streaming mode: 0 = Standard - Side-by-side streams if a webcam is attached
     * to Limelight, 1 = PiP Main - The secondary camera stream is placed in the lower-right corner of the primary
     * camera stream, 2 = PiP Secondary - The primary camera stream is placed in the lower-right corner of the
     * secondary camera stream.
     *
     * @property snapshot Allows users to take snapshots during a match: 0 = Reset snapshot mode, 1 = Take exactly
     * one snapshot.
     *
     * @property crop Sets the crop rectangle. The pipeline must utilize the default crop rectangle in the web
     * interface. The array must have exactly 4 entries.
     *
     * @constructor Gets the ledMode, camMode, pipeline, stream, snapshot, and crop.
     */
    constructor(
        ledMode: LedMode = LedMode.Pipeline,
        cameraMode: CameraMode = CameraMode.VisionProcessor,
        pipeline: Int = 0,
        streamMode: StreamMode = StreamMode.Standard,
        snapshotMode: SnapshotMode = SnapshotMode.Reset,
        crop: Array<Number> = arrayOf(0, 0, 0, 0)
    ) {
        if (pipeline < 0 || pipeline > 9)
            error("Invalid pipeline value")
        else if (crop.size != 4)
            error("Invalid crop array")

        limelight.getEntry("ledMode").setNumber(ledMode.ordinal)
        limelight.getEntry("camMode").setNumber(cameraMode.ordinal)
        limelight.getEntry("pipeline").setNumber(pipeline)
        limelight.getEntry("stream").setNumber(streamMode.ordinal)
        limelight.getEntry("snapshot").setNumber(snapshotMode.ordinal)
        limelight.getEntry("crop").setNumberArray(crop)
    }

    /**
     * @return Whether the limelight has any valid targets.
     */
    fun hasTarget(): Boolean = limelight.getEntry("tv").getDouble(0.0) == 1.0

    /**
     * @return Horizontal Offset From Crosshair To Target (-27 degrees to 27 degrees).
     */
    fun getHorizontalOffset(): Double = limelight.getEntry("tx").getDouble(0.0)

    /**
     * @return Vertical Offset From Crosshair To Target (-20.5 degrees to 20.5 degrees)
     */
    fun getVerticalOffset(): Double = limelight.getEntry("ty").getDouble(0.0)

    /**
     * @return Target Area (0% of image to 100% of image)
     */
    fun getArea(): Double = limelight.getEntry("ta").getDouble(0.0)

    /**
     * @return Distance from target (meters).
     */
    fun findTargetDistance(cameraHeight: Double, cameraAngle: Double, ballHeight: Double): Double =
        if (hasTarget()) (cameraHeight - ballHeight) * tan(Math.toRadians(getVerticalOffset() + cameraAngle)) else -1.0

    fun getColor(): Array<Number> = limelight.getEntry("tc").getNumberArray(arrayOf(-1))
}

enum class LedMode {
    Pipeline,
    ForceOff,
    ForceBlink,
    ForceOn,
}

enum class CameraMode {
    VisionProcessor,
    DriverCamera,
}

enum class StreamMode {
    Standard,
    SideBySide,
    PipMain,
    PipSecondary,
}

enum class SnapshotMode {
    Reset,
    One,
}