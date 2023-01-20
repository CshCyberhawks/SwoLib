package frc.robot

import com.ctre.phoenix.motorcontrol.can.TalonFX
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import cshcyberhawks.swolib.hardware.AnalogTurnEncoder
import cshcyberhawks.swolib.hardware.NavXGyro
import cshcyberhawks.swolib.math.Coordinate
import cshcyberhawks.swolib.swerve.SwerveDriveTrain
import cshcyberhawks.swolib.swerve.SwerveModule
import cshcyberhawks.swolib.swerve.configurations.fourwheelconfiguration.FourWheelSwerveConfiguration
import cshcyberhawks.swolib.swerve.configurations.fourwheelconfiguration.SwerveModuleConfiguration
import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.SPI
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.CommandScheduler

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
class Robot : TimedRobot() {
    val frontRightEncoder = AnalogTurnEncoder(Constants.frontRightEncoder, 81.6)
    val frontLeftEncoder = AnalogTurnEncoder(Constants.frontLeftEncoder, 311.2)
    val backRightEncoder = AnalogTurnEncoder(Constants.backRightEncoder, 103.4)
    val backLeftEncoder = AnalogTurnEncoder(Constants.backLeftEncoder, 113.5)

    val swerveModuleConfiguration = SwerveModuleConfiguration(4.0, 0.0505, 7.0)

    val frontRightSwerveModule = SwerveModule(
        TalonSRX(Constants.frontRightTurnMotor),
        TalonFX(Constants.frontRightDriveMotor),
        frontRightEncoder,
        PIDController(0.01, 0.0, 0.0),
        PIDController(0.01, 0.0, 0.0),
        swerveModuleConfiguration
    )
    val frontLeftSwerveModule = SwerveModule(
        TalonSRX(Constants.frontLeftTurnMotor),
        TalonFX(Constants.frontLeftDriveMotor),
        frontLeftEncoder,
        PIDController(0.01, 0.0, 0.0),
        PIDController(0.01, 0.0, 0.0),
        swerveModuleConfiguration
    )
    val backRightSwerveModule = SwerveModule(
        TalonSRX(Constants.backRightTurnMotor),
        TalonFX(Constants.backRightDriveMotor),
        backRightEncoder,
        PIDController(0.01, 0.0, 0.0),
        PIDController(0.01, 0.0, 0.0),
        swerveModuleConfiguration
    )
    val backLeftSwerveModule = SwerveModule(
        TalonSRX(Constants.backLeftTurnMotor),
        TalonFX(Constants.backLeftDriveMotor),
        backLeftEncoder,
        PIDController(0.01, 0.0, 0.0),
        PIDController(0.01, 0.0, 0.0),
        swerveModuleConfiguration
    )

    val gyro = NavXGyro(SPI.Port.kMXP)

    val driveTrain = SwerveDriveTrain(
        FourWheelSwerveConfiguration(
            frontRightSwerveModule,
            frontLeftSwerveModule,
            backRightSwerveModule,
            backLeftSwerveModule
        ), gyro
    )

    val joystick = Joystick(0)

    /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     */
    override fun robotInit() {
        // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
        // autonomous chooser on the dashboard.
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
    override fun autonomousInit() {
    }

    /**
     * This function is called periodically during autonomous.
     */
    override fun autonomousPeriodic() {}

    /**
     * This function is called once when teleop is enabled.
     */
    override fun teleopInit() {
        println("HFUEBFUE")
    }

    /**
     * This function is called periodically during operator control.
     */
    override fun teleopPeriodic() {
        if (joystick.trigger) {
            gyro.setYawOffset()
        }

        driveTrain.drive(Coordinate(joystick.x, joystick.y).apply { theta += 90 }, joystick.twist)
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
        SmartDashboard.putNumber("FrontLeftEncoder", frontLeftEncoder.getRaw())
        SmartDashboard.putNumber("FrontRightEncoder", frontRightEncoder.getRaw())
        SmartDashboard.putNumber("BackLeftEncoder", backLeftEncoder.getRaw())
        SmartDashboard.putNumber("BackRightEncoder", backRightEncoder.getRaw())
    }
}
