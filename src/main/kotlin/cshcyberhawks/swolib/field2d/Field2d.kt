package cshcyberhawks.swolib.field2d

import cshcyberhawks.swolib.math.FieldPosition
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.networktables.NTSendable
import edu.wpi.first.networktables.NTSendableBuilder
import edu.wpi.first.networktables.NetworkTable
import edu.wpi.first.util.sendable.SendableRegistry
import edu.wpi.first.wpilibj.DriverStation

class Field2d : NTSendable, AutoCloseable {
    companion object {
        fun toWPILIBFieldPosition(pos: FieldPosition): FieldPosition {
            return if (DriverStation.getAlliance() == DriverStation.Alliance.Blue) {
                FieldPosition(pos.y, -pos.x, pos.angle)
            } else {
                FieldPosition(16.54 - pos.y, pos.x, pos.angle + 180)
            }
        }

        fun fromWPILIBFieldPosition(pos: FieldPosition): FieldPosition {
            return if (DriverStation.getAlliance() == DriverStation.Alliance.Blue) {
                FieldPosition(-pos.y, pos.x, pos.angle)
            } else {
                FieldPosition(pos.y, -pos.x + 16.54, pos.angle - 180)
            }
        }
    }

    private var table: NetworkTable? = null
    val objectList: MutableList<FieldObject2d> = ArrayList()

    /** Constructor.  */
    init {
        val obj = FieldObject2d("Robot")
        objectList.add(obj)
        SendableRegistry.add(this, "Field")
    }

    override fun close() {
        for (obj in objectList) {
            obj.close()
        }
    }

    /**
     * Set the robot pose from x, y, and rotation.
     *
     * @param xMeters X location, in meters
     * @param yMeters Y location, in meters
     * @param rotation rotation
     */
    @Synchronized
    fun setRobotPose(xMeters: Double, yMeters: Double, rotation: Rotation2d?) {
        objectList[0].setPose(xMeters, yMeters, rotation)
    }

    @get:Synchronized
    @set:Synchronized
    var robotPose: Pose2d
        /**
         * Get the robot pose.
         *
         * @return 2D pose
         */
        get() = objectList[0].pose
        /**
         * Set the robot pose from a Pose object.
         *
         * @param pose 2D pose
         */
        set(pose) {
            objectList[0].pose = pose
        }

    /**
     * Get or create a field object.
     *
     * @param name The field object's name.
     * @return Field object
     */
    @Synchronized
    fun getObject(name: String): FieldObject2d {
        for (obj in objectList) {
            if (obj.name == name) {
                return obj
            }
        }
        val obj = FieldObject2d(name)
        objectList.add(obj)
        if (table != null) {
            synchronized(obj) {
                obj.entry = table!!.getDoubleArrayTopic(name).getEntry(doubleArrayOf())
            }
        }
        return obj
    }

    @get:Synchronized
    val robotObject: FieldObject2d
        /**
         * Get the robot object.
         *
         * @return Field object for robot
         */
        get() = objectList[0]

    override fun initSendable(builder: NTSendableBuilder) {
        builder.setSmartDashboardType("Field2d")
        synchronized(this) {
            table = builder.table
            if (table != null) {
                for (obj in objectList) {
                    synchronized(obj) {
                        obj.entry = table!!.getDoubleArrayTopic(obj.name).getEntry(doubleArrayOf())
                        obj.updateEntry(true)
                    }
                }
            }
        }
    }
}
