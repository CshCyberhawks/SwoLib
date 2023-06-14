// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import cshcyberhawks.swolib.autonomous.paths.AutoPathManager;
import cshcyberhawks.swolib.field2d.Field2d;
import cshcyberhawks.swolib.hardware.implementations.Pigeon2Gyro;
import cshcyberhawks.swolib.hardware.implementations.SparkMaxTurnMotor;
import cshcyberhawks.swolib.hardware.implementations.TalonFXDriveMotor;
import cshcyberhawks.swolib.hardware.interfaces.GenericGyro;
import cshcyberhawks.swolib.limelight.*;
import cshcyberhawks.swolib.swerve.configurations.FourWheelAngleConfiguration;
import cshcyberhawks.swolib.swerve.configurations.FourWheelSpeedConfiguration;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import cshcyberhawks.swolib.autonomous.*;
import cshcyberhawks.swolib.swerve.*;
import cshcyberhawks.swolib.swerve.configurations.FourWheelSwerveConfiguration;
import cshcyberhawks.swolib.swerve.configurations.SwerveModuleConfiguration;
import cshcyberhawks.swolib.math.*;

import java.util.HashMap;
import java.util.Optional;

public class Robot extends TimedRobot {


    private SwerveModuleConfiguration swerveConfiguration = new
            SwerveModuleConfiguration(4.0, 0.0505, 7.0);

    private PIDController drivePIDBackLeft = new PIDController(0.01, 0.0, 0.0);
    private PIDController turnPIDBackLeft = new PIDController(.012, 0.0, 0.0002);

    private PIDController drivePIDBackRight = new PIDController(0.01, 0.0, 0.0);
    private PIDController turnPIDBackRight = new PIDController(.012, 0.0, 0.0002);

    private PIDController drivePIDFrontLeft = new PIDController(0.01, 0.0, 0.0);
    private PIDController turnPIDFrontLeft = new PIDController(.012, 0.0, 0.0002);

    private PIDController drivePIDFrontRight = new PIDController(0.01, 0.0, 0.0);
    private PIDController turnPIDFrontRight = new PIDController(.012, 0.0, 0.0002);

    private Limelight limelightLeft = new Limelight("limelight-left", 0.134, 0.0,  "123-456-7890", 0.0, 0.0, LedMode.ForceOn, CameraMode.VisionProcessor, 1, StreamMode.Standard, SnapshotMode.Reset, new Number[]{0, 0, 0, 0}, 1);
    private Limelight limelightRight = new Limelight("limelight-right", 0.134, 0.0, "123-456-7890", 0.0, 0.0, LedMode.ForceOn, CameraMode.VisionProcessor, 1, StreamMode.Standard, SnapshotMode.Reset, new Number[]{0, 0, 0, 0}, 1);


    private SwerveWheel backLeft = new
            SwerveWheel(
            new TalonFXDriveMotor(0, "0"),
            new SparkMaxTurnMotor(1, 2, 45.0, "0"),
            drivePIDBackLeft,
            turnPIDBackLeft,
            swerveConfiguration
    );
    private SwerveWheel backRight = new
            SwerveWheel(
            new TalonFXDriveMotor(0, "0"),
            new SparkMaxTurnMotor(1, 2, 45.0, "0"),
            drivePIDBackRight,
            turnPIDBackRight,
            swerveConfiguration
    );

    private SwerveWheel frontLeft = new
            SwerveWheel(
            new TalonFXDriveMotor(0, "0"),
            new SparkMaxTurnMotor(1, 2, 45.0, "0"),
            drivePIDFrontLeft,
            turnPIDFrontLeft,
            swerveConfiguration
    );

    private SwerveWheel frontRight = new
            SwerveWheel(
            new TalonFXDriveMotor(0, "0"),
            new SparkMaxTurnMotor(1, 2, 45.0, "0"),
            drivePIDFrontRight,
            turnPIDFrontRight,
            swerveConfiguration
    );


    private GenericGyro gyro = new Pigeon2Gyro(0, 90.0, 0.0);

    private SwerveDriveTrain swerveDriveTrain =
            new SwerveDriveTrain(
                    new FourWheelSwerveConfiguration(
                            frontRight,
                            frontLeft,
                            backRight,
                            backLeft,
                            new FourWheelAngleConfiguration(131.6, -131.6, 48.4, -48.4),
                            new FourWheelSpeedConfiguration(.65, .65, .65, .65)
                    ),
                    gyro
            );

    private Field2d field2d = new Field2d();

    private SwerveOdometry swo =
            new SwerveOdometry(
                    swerveDriveTrain,
                    gyro,
                    1.0,
                    new Vector3(0.0, 0.0, 0.0),
                    new Limelight[]{limelightLeft},
                    true,
                    Optional.of(field2d)
            );

    private TrapezoidProfile.Constraints autoTrapConstraints = new TrapezoidProfile.Constraints(5.0, 3.0);
    private TrapezoidProfile.Constraints twistTrapConstraints = new TrapezoidProfile.Constraints(90.0, 20.0);

    private ProfiledPIDController autoPIDX = new ProfiledPIDController(1.0, 0.0, 0.01, autoTrapConstraints);
    private ProfiledPIDController autoPIDY = new ProfiledPIDController(1.0, 0.0, 0.01, autoTrapConstraints);
    private PIDController twistPID = new PIDController(0.1, 0.0, 0.00);

    private SwerveAuto auto =
            new SwerveAuto(
                    autoPIDX,
                    autoPIDY,
                    twistPID,
                    twistTrapConstraints,
                    10.0,
                    0.2,
                    swo,
                    swerveDriveTrain,
                    gyro,
                    true,
                    Optional.of(field2d)
            );

    private AutoPathManager autoPathManager = new AutoPathManager(auto, gyro, new HashMap<>());


    @Override
    public void robotInit() {
        //NOTE: here you might want to make sure your limelight pipelines are correctly set
        limelightLeft.setPipeline(1);
        limelightRight.setPipeline(1);

        limelightRight.setLED(LedMode.ForceOff);
        limelightLeft.setLED(LedMode.ForceOff);
    }

    @Override
    public void robotPeriodic() {
        swo.resetOdometryLL(0.0);
        swo.updatePosition();
    }

    @Override
    public void autonomousInit() {

        limelightRight.setLED(LedMode.ForceOn);
        limelightLeft.setLED(LedMode.ForceOn);

        autoPathManager.getPaths().get("path1").schedule();
    }

    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic() {}

    /**
     * This function is called once when teleop is enabled.
     */
    @Override
    public void teleopInit() {
        // NOTE: this will automatically set the field orientation's forward to whichever direction
        // the robot is currently facing
        gyro.setYawOffset(0.0);

        limelightRight.setLED(LedMode.ForceOn);
        limelightLeft.setLED(LedMode.ForceOn);
    }

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {
        // NOTE: replace the below zeroes with joystick/controller input to drive the robot in
        // teleop
        swerveDriveTrain.drive(new Vector2(0.0, 0.0), 0.0, false);
    }

    /**
     * This function is called once when test mode is enabled.
     */
    @Override
    public void testInit() {
    }

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {

        // NOTE: the below code can be used to read the raw encoder values from each twist wheel.
        // This is necessary to set the encoder offsets (inform them which direction 0/forward is).

        Double[] encoderValues = new Double[]{
                backLeft.getRawEncoder(),
                frontLeft.getRawEncoder(),
                frontRight.getRawEncoder(),
                backRight.getRawEncoder()
        };

        SmartDashboard.putString("Encoder Offsets", encoderValues.toString());
    }
}
