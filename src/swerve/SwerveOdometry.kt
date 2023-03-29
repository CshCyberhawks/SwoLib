package cshcyberhawks.swolib.swerve

import cshcyberhawks.swolib.field2d.Field2d
import cshcyberhawks.swolib.hardware.interfaces.GenericGyro
import cshcyberhawks.swolib.limelight.Limelight
import cshcyberhawks.swolib.math.*
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.networktables.GenericEntry
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.DriverStation.Alliance
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import java.util.Optional
import kotlin.math.cos
import kotlin.math.sin

/**
 * Class for odometry using a swerve drive train. This tracks the position of the robot in 2d/3d
 * space by un-doing SwerveDriveTrain (see SwerveDriveTrain.kt) and using the gyro to track the
 * robot's orientation. READ THE DOCUMENTATION FOR "swoToMeters" IT IS CRUCIAL.
 * @param swerveDriveTrain The swerve drive train to use for odometry
 * @param gyro The gyro to use for odometry
 * @param swoToMeters The conversion factor from swerve drive units to meters. This unit (named the
 * SWO) was accidentally discovered by team 2875 (Milan Lustig) and represents a semi-fluid unit of
 * measurement nearly equal to a meter. SwerveOdometry will position the robot in 2d space in this
 * unit, a conversion factor is required to turn it to meters. To discern this conversion factor, I
 * recommend settings this value to one, measuring out 10-50m (mark the floor), drive the robot in a
 * straight line at a medium speed (to ensure the wheels don't slip) along this distance. Then, you
 * can take the distance traveled reported by SwerveOdometry (with a conversion factor of one) and
 * divide it by the actual measured distance traveled. It is recommended to re-measure this on any
 * major changes in floor material (carpet vs tile) or with any changes to wheel treads, robot
 * weight, or motors. ALL HAIL THE SWO!
 * @param startingPosition The starting position of the robot
 * @param limelightList The list of limelights to use for odometry
 * @param debugLogging Whether or not to log debug information
 * @param field2d The field2d object to use for odometry
 * @see SwerveDriveTrain
 * @see GenericGyro
 * @see Limelight
 * @see Field2d
 */
class SwerveOdometry(
        private var swerveDriveTrain: SwerveDriveTrain,
        private var gyro: GenericGyro,
        private val swoToMeters: Double,
        startingPosition: Vector3 = Vector3(0.0, 0.0, 0.0),
        private val limelightList: Array<Limelight> = arrayOf(),
        private val debugLogging: Boolean = false,
        private val field2d: Optional<Field2d> = Optional.empty()
) {
    var fieldPosition = Vector3() + startingPosition
    private var lastTime = MiscCalculations.getCurrentTime()

    private val odometryShuffleTab: ShuffleboardTab = Shuffleboard.getTab("Odometry")
    private val xPosition: GenericEntry =
            odometryShuffleTab.add("X Position", 0.0).withPosition(0, 0).withSize(2, 1).entry
    private val yPosition: GenericEntry =
            odometryShuffleTab.add("Y Position", 0.0).withPosition(2, 0).withSize(2, 1).entry

    private var lastYawLLTime = 0.0

    fun getVelocity(): Vector3 {
        var total = Vector2()

        val wheelVectors = swerveDriveTrain.swerveConfiguration.getWheelVectors()
        for (wheel in wheelVectors) {
            val wheelVector = Vector2.fromPolar(wheel)
            total += wheelVector
        }

        total /= wheelVectors.size

        val polar = Polar.fromVector2(total)
        polar.theta += gyro.getYaw()
        total = Vector2.fromPolar(polar)

        // Pitch and roll might be flipped
        val x = total.x * cos(Math.toRadians(gyro.getPitch())) / swoToMeters
        val y = total.y * cos(Math.toRadians(gyro.getRoll())) / swoToMeters
        val z =
                (total.x * sin(Math.toRadians(gyro.getPitch())) +
                        total.y * sin(Math.toRadians(gyro.getRoll()))) / swoToMeters
        return Vector3(x, y, z)
        //        return Vector3(total.x, total.y, 0.0)
    }

    /** Updates the position of the robot (optionally using the limelight and fiducials) */
    fun updatePosition(
            resetFieldOrientationLimelight: Boolean = false,
            limelightFieldRotation: Double = 0.0
    ) {
        fieldPosition += getVelocity() * (MiscCalculations.getCurrentTime() - lastTime)

        for (limelight in limelightList) {
            val limelightPosition = limelight.getBotPosition()
            //            if (!limelightPosition.isEmpty) {
            //                val position = limelightPosition.get()
            //                val vectorPosition = if (DriverStation.getAlliance() ==
            // DriverStation.Alliance.Blue) {
            //                    Vector3(-position.x, -position.y, position.z)
            //                } else {
            //                    Vector3(-(position.x) - 16.54, -position.y, position.z)
            //                }

            //                fieldPosition = vectorPosition
            //            }

            if (resetFieldOrientationLimelight) {
                val limelightRotation = limelight.getBotYaw()
                if (!limelightRotation.isEmpty &&
                                MiscCalculations.getCurrentTime() - lastYawLLTime > 0.3
                ) {
                    SmartDashboard.putNumber("ll rotation", limelightRotation.get())
                    val rotation =
                            AngleCalculations.wrapAroundAngles(
                                    if (DriverStation.getAlliance() == Alliance.Red) {
                                        limelightRotation.get()
                                    } else {
                                        limelightRotation.get() + 180
                                    }
                            )

                    val limeRot = limelightFieldRotation
                    if (limeRot > 0) {
                        val offset = limelight.getBotYaw()
                        if (offset.isPresent) {
                            gyro.setYawOffset(limeRot - offset.get())
                        }
                    }

                    lastYawLLTime = MiscCalculations.getCurrentTime()
                    gyro.setYawOffset(rotation)
                }
            }
            //
            //            val limelightRotation = limelight.getBotYaw()
            //            if (!limelightRotation.isEmpty && MiscCalculations.getCurrentTime() -
            // lastYawLLTime > 0.3) {
            //                SmartDashboard.putNumber("ll rotation", limelightRotation.get())
            //                val rotation = AngleCalculations.wrapAroundAngles(if
            // (DriverStation.getAlliance() == Alliance.Red) {
            //                    limelightRotation.get()
            //                } else {
            //                    limelightRotation.get() + 180
            //                })
            //
            //
            //                lastYawLLTime = MiscCalculations.getCurrentTime()
            //                gyro.setYawOffset(rotation)
            //            }
        }

        if (debugLogging) {
            xPosition.setDouble(fieldPosition.x)
            yPosition.setDouble(fieldPosition.y)
        }

        updateField()
        lastTime = MiscCalculations.getCurrentTime()
    }

    private fun updateField() {
        if (!field2d.isEmpty) {
            val changedPosition =
                    Field2d.toWPILIBFieldPosition(
                            FieldPosition(fieldPosition.x, fieldPosition.y, gyro.getYaw())
                    )
            field2d.get()
                    .setRobotPose(
                            changedPosition.x,
                            changedPosition.y,
                            Rotation2d(changedPosition.angleRadians)
                    )
        }
    }
}
