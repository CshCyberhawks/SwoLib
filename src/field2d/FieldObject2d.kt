// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
package cshcyberhawks.swolib.field2d

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.trajectory.Trajectory
import edu.wpi.first.networktables.DoubleArrayEntry

/** Game field object on a Field2d.  */
class FieldObject2d(var name: String, initialPosition: Pose2d = Pose2d()) : AutoCloseable {
    var entry: DoubleArrayEntry? = null
    private val poseList: MutableList<Pose2d> = ArrayList()

    init {
        setPoses(initialPosition)
    }

    override fun close() {
        if (entry != null) {
            entry!!.close()
        }
    }

    /**
     * Set the pose from x, y, and rotation.
     *
     * @param xMeters X location, in meters
     * @param yMeters Y location, in meters
     * @param rotation rotation
     */
    @Synchronized
    fun setPose(xMeters: Double, yMeters: Double, rotation: Rotation2d?) {
        pose = Pose2d(xMeters, yMeters, rotation)
    }

    @get:Synchronized
    @set:Synchronized
    var pose: Pose2d
        /**
         * Get the pose.
         *
         * @return 2D pose
         */
        get() {
            updateFromEntry()
            return if (poseList.isEmpty()) {
                Pose2d()
            } else poseList[0]
        }
        /**
         * Set the pose from a Pose object.
         *
         * @param pose 2D pose
         */
        set(pose) {
            setPoses(pose)
        }

    /**
     * Set multiple poses from a list of Pose objects. The total number of poses is limited to 85.
     *
     * @param poses list of 2D poses
     */
    @Synchronized
    fun setPoses(vararg poses: Pose2d) {
        poseList.clear()
        for (pose in poses) {
            poseList.add(pose)
        }
        updateEntry()
    }

    /**
     * Sets poses from a trajectory.
     *
     * @param trajectory The trajectory from which the poses should be added.
     */
    @Synchronized
    fun setTrajectory(trajectory: Trajectory) {
        poseList.clear()
        for (state in trajectory.states) {
            poseList.add(state.poseMeters)
        }
        updateEntry()
    }

    @get:Synchronized
    @set:Synchronized
    var poses: List<Pose2d>
        /**
         * Get multiple poses.
         *
         * @return list of 2D poses
         */
        get() {
            updateFromEntry()
            return ArrayList(poseList)
        }
        /**
         * Set multiple poses from a list of Pose objects. The total number of poses is limited to 85.
         *
         * @param poses list of 2D poses
         */
        set(poses) {
            poseList.clear()
            for (pose in poses) {
                poseList.add(pose)
            }
            updateEntry()
        }

    fun updateEntry() {
        updateEntry(false)
    }

    @Synchronized
    fun updateEntry(setDefault: Boolean) {
        if (entry == null) {
            return
        }
        val arr = DoubleArray(poseList.size * 3)
        var ndx = 0
        for (pose in poseList) {
            val translation = pose.translation
            arr[ndx + 0] = translation.x
            arr[ndx + 1] = translation.y
            arr[ndx + 2] = pose.rotation.degrees
            ndx += 3
        }
        if (setDefault) {
            entry!!.setDefault(arr)
        } else {
            entry!!.set(arr)
        }
    }

    @Synchronized
    private fun updateFromEntry() {
        if (entry == null) {
            return
        }
        val arr: DoubleArray? = entry!!.get(null as DoubleArray?)
        if (arr != null) {
            if (arr.size % 3 != 0) {
                return
            }
            poseList.clear()
            var i = 0
            while (i < arr.size) {
                poseList.add(Pose2d(arr[i], arr[i + 1], Rotation2d.fromDegrees(arr[i + 2])))
                i += 3
            }
        }
    }
}