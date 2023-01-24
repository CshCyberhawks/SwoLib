package frc.robot

import com.ctre.phoenix.motorcontrol.can.TalonFX
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import cshcyberhawks.swolib.hardware.AnalogTurnEncoder
import cshcyberhawks.swolib.hardware.NavXGyro
import cshcyberhawks.swolib.math.Coordinate
import cshcyberhawks.swolib.math.MiscCalculations
import cshcyberhawks.swolib.math.Vector2
import cshcyberhawks.swolib.swerve.SwerveOdometry
import cshcyberhawks.swolib.swerve.configurations.FourWheelSwerveConfiguration
import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.SPI
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.CommandScheduler
import frc.robot.subsystems.SwerveDriveTrain
import frc.robot.subsystems.SwerveWheel

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
class Robot : TimedRobot() {
    var backLeft: SwerveWheel =
        SwerveWheel(
            Constants.backLeftTurnMotor,
            Constants.backLeftDriveMotor,
            Constants.backLeftEncoder
        )
    var backRight: SwerveWheel =
        SwerveWheel(
            Constants.backRightTurnMotor,
            Constants.backRightDriveMotor,
            Constants.backRightEncoder
        )
    var frontLeft: SwerveWheel =
        SwerveWheel(
            Constants.frontLeftTurnMotor,
            Constants.frontLeftDriveMotor,
            Constants.frontLeftEncoder
        )
    var frontRight: SwerveWheel =
        SwerveWheel(
            Constants.frontRightTurnMotor,
            Constants.frontRightDriveMotor,
            Constants.frontRightEncoder
        )

    val gyro = NavXGyro(SPI.Port.kMXP)

    val swerveDriveTrain = SwerveDriveTrain(FourWheelSwerveConfiguration(frontRight, frontLeft, backRight, backLeft), gyro)

    val swo = SwerveOdometry(swerveDriveTrain, gyro)

    val joystick = Joystick(0)

    /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     */
    override fun robotInit() {
        // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
        // autonomous chooser on the dashboard.
        gyro.setYawOffset()
    }

    /**
     * This function is called every robot packet, no matter the mode. Use this for items like
     * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
     *
     *
     * This runs after the mode specific periodic functions, but before
     * LiveWindow and SmartDashboard integrated updating.
     */
    override fun robotPeriodic() {
        // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
        // commands, running already-scheduled commands, removing finished or interrupted commands,
        // and running subsystem periodic() methods.  This must be called from the robot's periodic
        // block in order for anything in the Command-based framework to work.
        CommandScheduler.getInstance().run()
    }

    /**
     * This function is called once each time the robot enters Disabled mode.
     */
    override fun disabledInit() {}

    /**
     * This function is called periodically when disabled.
     */
    override fun disabledPeriodic() {}

    /**
     * This autonomous runs the autonomous command selected by your [RobotContainer] class.
     */
    override fun autonomousInit() {}

    /**
     * This function is called periodically during autonomous.
     */
    override fun autonomousPeriodic() {}

    /**
     * This function is called once when teleop is enabled.
     */
    override fun teleopInit() {}

    /**
     * This function is called periodically during operator control.
     */
    override fun teleopPeriodic() {
        if (joystick.trigger) {
            gyro.setYawOffset()
        }

        swerveDriveTrain.drive(Vector2(joystick.y, joystick.x), joystick.twist)
        swo.updatePosition()

        SmartDashboard.putNumber("Gyro Pitch", gyro.getPitch())
        SmartDashboard.putNumber("Gyro Roll", gyro.getRoll())

        SmartDashboard.putNumber("Field Pos X", swo.fieldPosition.x)
        SmartDashboard.putNumber("Field Pos Y", swo.fieldPosition.y)
        SmartDashboard.putNumber("Field Pos Z", swo.fieldPosition.z)
    }

    /**
     * This function is called once when test mode is enabled.
     */
    override fun testInit() {
        // Cancels all running commands at the start of test mode.
        CommandScheduler.getInstance().cancelAll()
    }

    /**
     * This function is called periodically during test mode.
     */
    override fun testPeriodic() {
//        SmartDashboard.putNumber("FrontLeftEncoder", frontLeftEncoder.getRaw())
//        SmartDashboard.putNumber("FrontRightEncoder", frontRightEncoder.getRaw())
//        SmartDashboard.putNumber("BackLeftEncoder", backLeftEncoder.getRaw())
//        SmartDashboard.putNumber("BackRightEncoder", backRightEncoder.getRaw())
//
//        val encoderValues = arrayOf(
//            frontLeftEncoder.getRaw(),
//            frontRightEncoder.getRaw(),
//            backLeftEncoder.getRaw(),
//            backRightEncoder.getRaw()
//        )
//
//        SmartDashboard.putString("Encoder Values", encoderValues.joinToString(", "))
        swerveDriveTrain.logEncoderValues()
    }
}
