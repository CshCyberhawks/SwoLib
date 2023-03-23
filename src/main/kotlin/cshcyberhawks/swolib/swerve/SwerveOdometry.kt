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
import frc.robot.util.JoyIO
import java.util.Optional
import kotlin.math.cos
import kotlin.math.sin

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
    private val xPosition: GenericEntry = odometryShuffleTab.add("X Position", 0.0).withPosition(0, 0).withSize(2, 1).entry
    private val yPosition: GenericEntry = odometryShuffleTab.add("Y Position", 0.0).withPosition(2, 0).withSize(2, 1).entry

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

    fun updatePosition() {
        fieldPosition += getVelocity() * (MiscCalculations.getCurrentTime() - lastTime)

        for (limelight in limelightList) {
            val limelightPosition = limelight.getBotPosition()
//            if (!limelightPosition.isEmpty) {
//                val position = limelightPosition.get()
//                val vectorPosition = if (DriverStation.getAlliance() == DriverStation.Alliance.Blue) {
//                    Vector3(-position.x, -position.y, position.z)
//                } else {
//                    Vector3(-(position.x) - 16.54, -position.y, position.z)
//                }

//                fieldPosition = vectorPosition
//            }

            if (JoyIO.resetFieldLimelight) {
                val limelightRotation = limelight.getBotYaw()
                if (!limelightRotation.isEmpty && MiscCalculations.getCurrentTime() - lastYawLLTime > 0.3) {
                    SmartDashboard.putNumber("ll rotation", limelightRotation.get())
                    val rotation = AngleCalculations.wrapAroundAngles(if (DriverStation.getAlliance() == Alliance.Red) {
                        limelightRotation.get()
                    } else {
                        limelightRotation.get() + 180
                    })

                    val limeRot = JoyIO.limelightChangeRot
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
//            if (!limelightRotation.isEmpty && MiscCalculations.getCurrentTime() - lastYawLLTime > 0.3) {
//                SmartDashboard.putNumber("ll rotation", limelightRotation.get())
//                val rotation = AngleCalculations.wrapAroundAngles(if (DriverStation.getAlliance() == Alliance.Red) {
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
            val changedPosition = Field2d.toWPILIBFieldPosition(FieldPosition(fieldPosition.x, fieldPosition.y, gyro.getYaw()))
            field2d.get().setRobotPose(changedPosition.x, changedPosition.y, Rotation2d(changedPosition.angleRadians))
        }
    }
}
