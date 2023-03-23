package cshcyberhawks.swolib.autonomous

import cshcyberhawks.swolib.field2d.Field2d
import cshcyberhawks.swolib.field2d.FieldObject2d
import cshcyberhawks.swolib.hardware.interfaces.GenericGyro
import cshcyberhawks.swolib.math.AngleCalculations
import cshcyberhawks.swolib.math.FieldPosition
import cshcyberhawks.swolib.math.MiscCalculations
import cshcyberhawks.swolib.math.Vector2
import cshcyberhawks.swolib.swerve.SwerveDriveTrain
import cshcyberhawks.swolib.swerve.SwerveOdometry
import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.math.controller.ProfiledPIDController
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.trajectory.TrapezoidProfile
import edu.wpi.first.networktables.GenericEntry
import edu.wpi.first.util.WPIUtilJNI
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import java.util.Optional
import kotlin.math.abs

class SwerveAuto(
    private val xPID: ProfiledPIDController,
    private val yPID: ProfiledPIDController,
    private val twistPID: PIDController,
    private val twistTrapConstraints: TrapezoidProfile.Constraints,
    private val angleDeadzone: Double,
    private val positionDeadzone: Double,
    val swo: SwerveOdometry,
    val swerveSystem: SwerveDriveTrain,
    val gyro: GenericGyro,
    private val debugLogging: Boolean = false,
    private val field2d: Optional<Field2d> = Optional.empty()
) {
    var desiredPosition: FieldPosition = FieldPosition(0.0, 0.0, 0.0)
        set(value) {
            xPID.goal = TrapezoidProfile.State(value.x, 0.0)
            yPID.goal = TrapezoidProfile.State(value.y, 0.0)
            xPID.reset(swo.fieldPosition.x)
            yPID.reset(swo.fieldPosition.y)
            twistPID.setpoint = value.angle
            twistPID.reset()
            desiredTwistTrap = TrapezoidProfile.State(value.angle, 0.0)
            currentTwistTrap = TrapezoidProfile.State(gyro.getYaw(), 0.0)
            SmartDashboard.putNumber("Desired X", value.x)
            SmartDashboard.putNumber("Desired Y", value.y)
            if (!field2d.isEmpty) {
                val changedPos = Field2d.toWPILIBFieldPosition(FieldPosition(value.x, value.y, value.angle))
                field2d.get().getObject("DesiredPosition").pose = Pose2d(Translation2d(changedPos.x, changedPos.y), Rotation2d(changedPos.angleRadians))
            }
            xDesPosShuffle.setDouble(value.x)
            yDesPosShuffle.setDouble(value.y)
            prevTime = 0.0
            field = value
        }

    private var desiredTwistTrap = TrapezoidProfile.State(desiredPosition.angle, 0.0)
    private var currentTwistTrap = TrapezoidProfile.State(gyro.getYaw(), 0.0)

    private var prevTime = 0.0

    fun setDesiredEndVelocity(velo: Vector2) {
        this.xPID.goal = TrapezoidProfile.State(xPID.goal.position, velo.x)
        this.yPID.goal = TrapezoidProfile.State(yPID.goal.position, velo.y)
    }

    private val autoShuffleboardTab: ShuffleboardTab = Shuffleboard.getTab("Auto")

    // make all the shuffleboard items entries
    //    val xPIDShuffleboard = autoShuffleboardTab.add("X PID", xPID)
    //    val yPIDShuffleboard = autoShuffleboardTab.add("Y PID", yPID)
    //    val twistPIDShuffleboard = autoShuffleboardTab.add("Twist PID", twistPID)

    private val translationTwistShuffleboard: GenericEntry =
        autoShuffleboardTab.add("Translation Twist", 0.0).entry

    private val xPIDOutputShuffle: GenericEntry = autoShuffleboardTab.add("X PID OUT", 0.0).entry
    private val yPIDOutputShuffle = autoShuffleboardTab.add("Y PID OUT", 0.0).entry
    private val twistPIDOutputShuffle: GenericEntry =
        autoShuffleboardTab.add("Twist PID OUT", 0.0).entry

    private val xDesPosShuffle = autoShuffleboardTab.add("Desired Pos X", 0.0).entry
    private val yDesPosShuffle = autoShuffleboardTab.add("Desired Pos Y", 0.0).entry



    init {
        twistPID.enableContinuousInput(0.0, 360.0)

//        if (!field2d.isEmpty) {
//            field2d.get().objectList.add(FieldObject2d("Desired Position"))
//        }
    }

    private fun calculateTwist(): Double {
        val timeNow = WPIUtilJNI.now() * 1.0e-6
        val trapTime: Double = if (prevTime == 0.0) 0.0 else timeNow - prevTime

        // ryan suggested this
        val pidVal = twistPID.calculate(gyro.getYaw()) / 360

        val trapProfile = TrapezoidProfile(twistTrapConstraints, desiredTwistTrap, currentTwistTrap)

        val trapOutput = trapProfile.calculate(trapTime)

        val twistOutput =
            if (pidVal < 0) {
                pidVal - abs(trapOutput.velocity)
            } else {
                pidVal + abs(trapOutput.velocity)
            }

        SmartDashboard.putNumber("Twist PID", pidVal)
        SmartDashboard.putNumber("Twist Trap", trapOutput.velocity)
        SmartDashboard.putNumber("Twist Des", desiredPosition.angle)
        SmartDashboard.putNumber("Twist Out", twistOutput)

        if (debugLogging) {
            twistPIDOutputShuffle.setDouble(pidVal)
        }

        prevTime = timeNow

        return -twistOutput
    }

    private fun calculateTranslation(): Vector2 {
        val xPIDOutput = xPID.calculate(swo.fieldPosition.x)
        val yPIDOutput = yPID.calculate(swo.fieldPosition.y)

        SmartDashboard.putNumber("X PID", xPIDOutput)
        SmartDashboard.putNumber("Y PID", yPIDOutput)

        if (debugLogging) {
            xPIDOutputShuffle.setDouble(xPIDOutput)
            yPIDOutputShuffle.setDouble(yPIDOutput)
        }

        return Vector2(xPIDOutput, yPIDOutput)
    }

    // twists and translates
    fun move() {
        var translation: Vector2 = Vector2(0.0, 0.0)
        var twist: Double = 0.0

        SmartDashboard.putBoolean("At Des Pos", isAtDesiredPosition())
        if (!isAtDesiredPosition()) {
            translation = calculateTranslation()
        }

        val atDesiredAngle = isAtDesiredAngle()
        SmartDashboard.putBoolean("At Des Angle", atDesiredAngle)
        if (!atDesiredAngle) {
            twist = calculateTwist()
        }

        if (debugLogging) {
            translationTwistShuffleboard.setDouble(twist)
        }

        swerveSystem.drive(translation, twist)
    }

    private fun isAtDesiredAngle(): Boolean {
        return AngleCalculations.wrapAroundAngles(desiredPosition.angle - gyro.getYaw()) <
            angleDeadzone ||
            AngleCalculations.wrapAroundAngles(desiredPosition.angle - gyro.getYaw()) >
            360 - angleDeadzone
    }

    private fun isAtDesiredPosition(): Boolean {
        return (MiscCalculations.calculateDeadzone(
            desiredPosition.x - swo.fieldPosition.x,
            positionDeadzone
        ) == 0.0 &&
            MiscCalculations.calculateDeadzone(
                desiredPosition.y - swo.fieldPosition.y,
                positionDeadzone
            ) == 0.0)
    }

    fun setDesiredAngleRelative(desiredAngle: Double) {
        desiredPosition =
            FieldPosition(
                desiredPosition.x,
                desiredPosition.y,
                AngleCalculations.wrapAroundAngles(gyro.getYaw() + desiredAngle)
            )
    }

    fun isFinishedMoving(): Boolean {
        return isAtDesiredAngle() && isAtDesiredPosition()
    }

    fun kill() {
        swerveSystem.drive(Vector2(0.0, 0.0), 0.0)
    }
}
