import cshcyberhawks.swolib.autonomous.SwerveAuto
import cshcyberhawks.swolib.autonomous.paths.AutoPathManager
import cshcyberhawks.swolib.field2d.Field2d
import cshcyberhawks.swolib.hardware.implementations.Pigeon2Gyro
import cshcyberhawks.swolib.hardware.implementations.SparkMaxTurnMotor
import cshcyberhawks.swolib.hardware.implementations.TalonFXDriveMotor
import cshcyberhawks.swolib.limelight.LedMode
import cshcyberhawks.swolib.limelight.Limelight
import cshcyberhawks.swolib.math.MiscCalculations
import cshcyberhawks.swolib.math.Vector2
import cshcyberhawks.swolib.math.Vector3
import cshcyberhawks.swolib.swerve.SwerveDriveTrain
import cshcyberhawks.swolib.swerve.SwerveOdometry
import cshcyberhawks.swolib.swerve.SwerveWheel
import cshcyberhawks.swolib.swerve.configurations.FourWheelAngleConfiguration
import cshcyberhawks.swolib.swerve.configurations.FourWheelSpeedConfiguration
import cshcyberhawks.swolib.swerve.configurations.FourWheelSwerveConfiguration
import cshcyberhawks.swolib.swerve.configurations.SwerveModuleConfiguration
import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.math.controller.ProfiledPIDController
import edu.wpi.first.math.trajectory.TrapezoidProfile
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import java.util.*

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
class Robot : TimedRobot() {
    private val swerveConfiguration: SwerveModuleConfiguration =
            SwerveModuleConfiguration(4.0, 0.0505, 7.0)

    private val drivePIDBackLeft = PIDController(0.01, 0.0, 0.0)
    private val turnPIDBackLeft = PIDController(.012, 0.0, 0.0002)

    private val drivePIDBackRight = PIDController(0.01, 0.0, 0.0)
    private val turnPIDBackRight = PIDController(.012, 0.0, 0.0002)

    private val drivePIDFrontLeft = PIDController(0.01, 0.0, 0.0)
    private val turnPIDFrontLeft = PIDController(.012, 0.0, 0.0002)

    private val drivePIDFrontRight = PIDController(0.01, 0.0, 0.0)
    private val turnPIDFrontRight = PIDController(.012, 0.0, 0.0002)

    private val limelightLeft =
            Limelight("limelight-left", 0.134, "123-456-7890", 0.0, fiducialPipeline = 1)
    private val limelightRight =
            Limelight("limelight-right", 0.12, "123-456-7890", 0.0, fiducialPipeline = 1)
    private var backLeft: SwerveWheel =
            SwerveWheel(
                    TalonFXDriveMotor(0),
                    SparkMaxTurnMotor(1, 2, 45.0),
                    drivePIDBackLeft,
                    turnPIDBackLeft,
                    swerveConfiguration
            )
    private var backRight: SwerveWheel =
            SwerveWheel(
                    TalonFXDriveMotor(0),
                    SparkMaxTurnMotor(1, 2, 45.0),
                    drivePIDBackRight,
                    turnPIDBackRight,
                    swerveConfiguration
            )
    private var frontLeft: SwerveWheel =
            SwerveWheel(
                    TalonFXDriveMotor(0),
                    SparkMaxTurnMotor(
                            1,
                            2,
                            45.0,
                    ),
                    drivePIDFrontLeft,
                    turnPIDFrontLeft,
                    swerveConfiguration
            )
    private var frontRight: SwerveWheel =
            SwerveWheel(
                    TalonFXDriveMotor(0),
                    SparkMaxTurnMotor(
                            1,
                            2,
                            45.0,
                    ),
                    drivePIDFrontRight,
                    turnPIDFrontRight,
                    swerveConfiguration
            )

    val gyro = Pigeon2Gyro(0, 90.0, 0.0)

    private val swerveDriveTrain =
            SwerveDriveTrain(
                    FourWheelSwerveConfiguration(
                            frontRight,
                            frontLeft,
                            backRight,
                            backLeft,
                            angleConfiguration =
                                    FourWheelAngleConfiguration(131.6, -131.6, 48.4, -48.4),
                            speedConfiguration = FourWheelSpeedConfiguration(.65, .65, .65, .65)
                    ),
                    gyro
            )

    private val field2d = Field2d()

    private val swo =
            SwerveOdometry(
                    swerveDriveTrain,
                    gyro,
                    1.0,
                    Vector3(0.0, 0.0, 0.0),
                    arrayOf(limelightLeft),
                    debugLogging = true,
                    field2d = Optional.of(field2d)
            )

    private val autoTrapConstraints = TrapezoidProfile.Constraints(5.0, 3.0)
    private val twistTrapConstraints = TrapezoidProfile.Constraints(90.0, 20.0)

    private val autoPIDX = ProfiledPIDController(1.0, 0.0, 0.01, autoTrapConstraints)
    private val autoPIDY = ProfiledPIDController(1.0, 0.0, 0.01, autoTrapConstraints)
    private val twistPID = PIDController(0.1, 0.0, 0.00)

    private val auto =
            SwerveAuto(
                    autoPIDX,
                    autoPIDY,
                    twistPID,
                    twistTrapConstraints,
                    // TrapezoidProfile.Constraints(4.0, 1.5),
                    10.0, // TODO: Tune PIDs so this can be smaller
                    0.2,
                    swo,
                    swerveDriveTrain,
                    gyro,
                    true,
                    Optional.of(field2d)
            )

    private val autoPathManager = AutoPathManager(auto, gyro)

    /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     */
    override fun robotInit() {
        // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
        // autonomous chooser on the dashboard.
        //NOTE: here you might want to make sure your limelight pipelines are correctly set
        limelightLeft.pipeline = 1
        limelightRight.pipeline = 1

        limelightRight.setLED(LedMode.ForceOff)
        limelightLeft.setLED(LedMode.ForceOff)
    }

    /**
     * This function is called every robot packet, no matter the mode. Use this for items like
     * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
     *
     * This runs after the mode specific periodic functions, but before LiveWindow and
     * SmartDashboard integrated updating.
     */
    override fun robotPeriodic() {
        swo.resetOdometryLL()
        swo.updatePosition()
    }

    /** This function is called once each time the robot enters Disabled mode. */
    override fun disabledInit() {}

    /** This function is called periodically when disabled. */
    override fun disabledPeriodic() {}

    /** This autonomous runs the autonomous command selected by your [RobotContainer] class. */
    override fun autonomousInit() {

        limelightRight.setLED(LedMode.ForceOn)
        limelightLeft.setLED(LedMode.ForceOn)
    }

    /** This function is called periodically during autonomous. */
    override fun autonomousPeriodic() {}

    /** This function is called once when teleop is enabled. */
    override fun teleopInit() {

        // NOTE: this will automatically set the field orientation's forward to whichever direction
        // the robot is currently facing
        gyro.setYawOffset()

        limelightRight.setLED(LedMode.ForceOn)
        limelightLeft.setLED(LedMode.ForceOn)
    }

    /** This function is called periodically during operator control. */
    override fun teleopPeriodic() {
        // NOTE: replace the below zeroes with joystick/controller input to drive the robot in
        // teleop
        swerveDriveTrain.drive(Vector2(0.0, 0.0), 0.0)
    }

    /** This function is called once when test mode is enabled. */
    override fun testInit() {}

    /** This function is called periodically during test mode. */
    override fun testPeriodic() {
        // NOTE: the below code can be used to read the raw encoder values from each twist wheel.
        // This is necessary to set the encoder offsets (inform them which direction 0/forward is).

        val encoderValues =
                arrayOf(
                        backLeft.getRawEncoder(),
                        frontLeft.getRawEncoder(),
                        frontRight.getRawEncoder(),
                        backRight.getRawEncoder()
                )

        SmartDashboard.putString("Encoder Offsets", encoderValues.joinToString(", "))
    }
}
