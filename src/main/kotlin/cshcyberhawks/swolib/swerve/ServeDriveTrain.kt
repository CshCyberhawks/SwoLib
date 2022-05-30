package cshcyberhawks.swolib.swerve

import cshcyberhawks.swolib.hardware.NavXGyro
import cshcyberhawks.swolib.math.MiscCalculations
import cshcyberhawks.swolib.math.Vector2
import edu.wpi.first.util.WPIUtilJNI
import edu.wpi.first.wpilibj2.command.SubsystemBase

/**
 * A class to encapsulate an entire 4 wheeled swerve drive train. The swerve modules are SwoLib
 * module and this drive train uses a NavX gyro for field orientation. This class also uses a set of
 * constants, we recommend leaving that constructor parameter empty and using the default values.
 *
 * @property backLeftModule The back left swerve module in the drive train
 *
 * @property backRightModule The back right swerve module in the drive train
 *
 * @property frontLeftModule The front left swerve module in the drive train
 *
 * @property frontRightModule The front right swerve module in the drive train
 *
 * @property gyro The navx gyro used by the swerve drive train for field orientation
 *
 * @property moduleConstants A set of constants used by the drive train for swerve calculations. It
 * is reccommended to leave this empty and use defualt values
 */
class SwerveDriveTrain(
        var backLeftModule: SwerveModule,
        var backRightModule: SwerveModule,
        var frontLeftModule: SwerveModule,
        var frontRightModule: SwerveModule,
        var gyro: NavXGyro,
        var moduleConstants: ModuleConstants = ModuleConstants(45, 135, -45, -135, .6, .6, .6, .6)
) : SubsystemBase() {

    var throttle = 0.35
    var predictedVelocity: Vector2
    var moduleArr = arrayOfNulls<SwerveModule>(4)
    var isTwisting = false

    // I do this to prevent large jumps in value with first run of loop in predicted
    // odometry
    private var lastUpdateTime = -1.0
    var maxSwos = 13.9458
    var maxMeters = 3.777

    init {
        gyro.setOffset()

        moduleArr[0] = backLeftModule
        moduleArr[1] = backRightModule
        moduleArr[2] = frontLeftModule
        moduleArr[3] = frontRightModule
        predictedVelocity = Vector2(0, 0)
    }

    fun polarToCartesian(theta: Double, r: Double): DoubleArray {
        // math to turn polar coordinate into cartesian
        val x = r * Math.cos(Math.toRadians(theta))
        val y = r * Math.sin(Math.toRadians(theta))
        return doubleArrayOf(x, y)
    }

    fun cartesianToPolar(x: Double, y: Double): DoubleArray {
        // math to turn cartesian into polar
        val r = Math.sqrt(Math.pow(x, 2.0) + Math.pow(y, 2.0))
        val theta = Math.toDegrees(Math.atan2(y, x))
        return doubleArrayOf(theta, r)
    }

    fun fieldOriented(x: Double, y: Double, gyroAngle: Double): DoubleArray {
        // turns the translation input into polar
        val polar = cartesianToPolar(x, y)
        // subtracts the gyro angle from the polar angle of the translation of the robot
        // makes it field oriented
        val theta = polar[0] + gyroAngle
        val r = polar[1]

        // returns the new field oriented translation but converted to cartesian
        return polarToCartesian(theta, r)
    }

    fun calculateDrive(
            x1: Double,
            y1: Double,
            theta2: Double,
            r2: Double,
            twistMult: Double
    ): DoubleArray {
        // X is 0 and Y is 1
        // Gets the cartesian coordinate of the robot's joystick translation inputs
        val driveCoordinate = fieldOriented(x1, y1, gyro.getAngle().toDouble())
        // Turns the twist constant + joystick twist input into a cartesian coordinates
        val twistCoordinate = polarToCartesian(theta2, r2 * twistMult)

        // Args are theta, r
        // Vector math adds the translation and twisting cartesian coordinates before
        // turning them into polar and returning
        // can average below instead of add - need to look into it
        return cartesianToPolar(
                driveCoordinate[0] + twistCoordinate[0],
                driveCoordinate[1] + twistCoordinate[1]
        )
    }

    fun drive(
            _inputX: Double,
            _inputY: Double,
            _inputTwist: Double,
            throttleChange: Double,
            mode: DriveState?
    ) {

        var inputX = _inputX
        var inputY = _inputY
        var inputTwist = _inputTwist

        val timeNow = WPIUtilJNI.now() * 1.0e-6
        val gyroAngle: Double = gyro.getAngle().toDouble()
        throttle = throttleChange

        if (inputX == 0.0 && inputY == 0.0 && inputTwist == 0.0) {
            backRightModule.preserveAngle()
            backLeftModule.preserveAngle()
            frontRightModule.preserveAngle()
            frontLeftModule.preserveAngle()
            lastUpdateTime = timeNow
            return
        }

        // random decimal below is the max speed of robot in swos
        // double constantScaler = 13.9458 * highestSpeed;
        if (mode == DriveState.teleop) {
            inputX *= throttle
            inputY *= throttle
            inputTwist *= throttle // (throttle * 3);
        }

        isTwisting = inputTwist != 0.0

        // calculates the speed and angle for each motor
        val frontRightVector =
                calculateDrive(
                        inputX,
                        inputY,
                        moduleConstants.frontRightTwistAngle.toDouble(),
                        inputTwist,
                        moduleConstants.frontRightTwistSpeed.toDouble()
                )
        val frontLeftVector =
                calculateDrive(
                        inputX,
                        inputY,
                        moduleConstants.frontLeftTwistAngle.toDouble(),
                        inputTwist,
                        moduleConstants.frontLeftTwistSpeed.toDouble()
                )
        val backRightVector =
                calculateDrive(
                        inputX,
                        inputY,
                        moduleConstants.backRightTwistAngle.toDouble(),
                        inputTwist,
                        moduleConstants.backRightTwistSpeed.toDouble()
                )
        val backLeftVector =
                calculateDrive(
                        inputX,
                        inputY,
                        moduleConstants.backLeftTwistAngle.toDouble(),
                        inputTwist,
                        moduleConstants.backLeftTwistSpeed.toDouble()
                )

        val frontRightSpeed = frontRightVector[1]
        val frontLeftSpeed = frontLeftVector[1]
        val backRightSpeed = backRightVector[1]
        val backLeftSpeed = backLeftVector[1]
        val frontRightAngle = frontRightVector[0]
        val frontLeftAngle = frontLeftVector[0]
        val backRightAngle = backRightVector[0]
        val backLeftAngle = backLeftVector[0]
        var moduleSpeeds =
                doubleArrayOf(frontRightSpeed, frontLeftSpeed, backRightSpeed, backLeftSpeed)
        moduleSpeeds = MiscCalculations.normalizeSpeeds(moduleSpeeds, 1.0, -1.0)

        // sets the speed and angle of each motor
        backRightModule.drive(moduleSpeeds[2], backRightAngle)
        backLeftModule.drive(moduleSpeeds[3], backLeftAngle)
        frontRightModule.drive(moduleSpeeds[0], frontRightAngle)
        frontLeftModule.drive(moduleSpeeds[1], frontLeftAngle)

        lastUpdateTime = timeNow
    }
}
