package frc.robot.subsystems

import cshcyberhawks.swolib.hardware.GenericGyro
import cshcyberhawks.swolib.math.Polar
import cshcyberhawks.swolib.math.Vector2
import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.Constants
import kotlin.math.*

class SwerveDriveTrain(val gyro: GenericGyro) : SubsystemBase() { // p = 10 gets oscillation
    companion object {
        fun normalizeWheelSpeeds(wheelVectors: Array<Double>, distanceFromZero: Double): Array<Double> {
            var max = abs(wheelVectors[0])

            for (wheelVector in wheelVectors) {
                if (abs(wheelVector) > max) {
                    max = abs(wheelVector)
                }
            }

            val maxSpeed = if (max > distanceFromZero) max else distanceFromZero

            for (i in wheelVectors.indices) {
                wheelVectors[i] = wheelVectors[i] / maxSpeed * distanceFromZero
            }

            return wheelVectors
        }
    }

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


    var xPID: PIDController = PIDController(1.2, 0.0, 0.2)
    var yPID: PIDController = PIDController(1.2, 0.0, 0.2)

    // .3, 0.0, 0.01
    var twistPID: PIDController = PIDController(2.0, 0.0, 0.1)

    var wheelArr: Array<SwerveWheel> = arrayOf(backLeft, backRight, frontLeft, frontRight)

    var isTwisting = false

    // I do this to prevent large jumps in value with first run of loop in predicted
    // odometry
    private var lastUpdateTime = -1.0

    // var maxSwos = 13.9458
    // var maxMeters = 3.777

    init {
        gyro.setYawOffset()
    }

    fun polarToCartesian(theta: Double, r: Double): DoubleArray {
        // math to turn polar coordinate into cartesian
        val x = r * cos(Math.toRadians(theta))
        val y = r * sin(Math.toRadians(theta))
        return doubleArrayOf(x, y)
    }

    fun cartesianToPolar(x: Double, y: Double): DoubleArray {
        // math to turn cartesian into polar
        val r = sqrt(Math.pow(x, 2.0) + y.pow(2.0))
        val theta = Math.toDegrees(atan2(y, x))
        return doubleArrayOf(theta, r)
    }

    fun fieldOriented(input: Vector2, gyroAngle: Double): Vector2 {
        val polar = Polar.fromVector2(input)
        polar.theta += gyroAngle
        return Vector2.fromPolar(polar)
    }

    fun calculateDrive(
            driveCord: Vector2,
            twistCord: Vector2,
    ): Polar {
        // X is 0 and Y is 1
        // Gets the cartesian coordinate of the robot's joystick translation inputs
        //        SmartDashboard.putBoolean("Field Oriented", fieldOrientedEnabled)
        val driveCoordinate = fieldOriented(driveCord, gyro.getYaw())
        // Turns the twist constant + joystick twist input into a cartesian coordinates

        // Args are theta, r
        // Vector math adds the translation and twisting cartesian coordinates before
        // turning them into polar and returning
        // can average below instead of add - need to look into it
        return Polar.fromVector2(
                Vector2(driveCoordinate.x + twistCord.x,
                driveCoordinate.y + twistCord.y)
        )
    }

    fun drive(
            input: Vector2,
            inputTwist: Double,
    ) {
        if (input == Vector2() && inputTwist == 0.0) {
            backRight.preserveAngle()
            backLeft.preserveAngle()
            frontRight.preserveAngle()
            frontLeft.preserveAngle()
            return
        }


        SmartDashboard.putNumber("in twist", inputTwist)


        isTwisting = inputTwist != 0.0

        // SmartDashboard.putNumber("drive inputX ", inputX)
        // SmartDashboard.putNumber("drive inputY ", inputY)

        val twistMult = 0.5;

        // calculates the speed and angle for each motor
        val frontRightVector =
                calculateDrive(
                        input,
                    Vector2.fromPolar( Polar(
                            45.0,
                inputTwist * twistMult)
                ))
        val frontLeftVector =
                calculateDrive(
                    input,
                    Vector2.fromPolar(Polar(
                        135.0,
                        inputTwist * twistMult))
                )
        val backRightVector =
                calculateDrive(
                    input,
                    Vector2.fromPolar(Polar(
                        -45.0,
                        inputTwist* twistMult ))
                )
        val backLeftVector =
                calculateDrive(
                    input,
                    Vector2.fromPolar(Polar(
                        -135.0,
                        inputTwist*twistMult))
                )
        val frontRightSpeed = frontRightVector.r
        val frontLeftSpeed = frontLeftVector.r
        val backRightSpeed = backRightVector.r
        val backLeftSpeed = backLeftVector.r
        val frontRightAngle = frontRightVector.theta
        val frontLeftAngle = frontLeftVector.theta
        val backRightAngle = backRightVector.theta
        val backLeftAngle = backLeftVector.theta
        var wheelSpeeds =
                arrayOf(frontRightSpeed, frontLeftSpeed, backRightSpeed, backLeftSpeed)
        wheelSpeeds = normalizeWheelSpeeds(wheelSpeeds, 1.0)

        // SmartDashboard.putNumber("frontRightAngle", frontRightAngle)
        // SmartDashboard.putNumber("frontLeftAngle", frontLeftAngle)
        // SmartDashboard.putNumber("backRightAngle", backRightAngle)
        // SmartDashboard.putNumber("backLeftAngle", backLeftAngle)

        // sets the speed and angle of each motor
        backRight.drive(wheelSpeeds[2], backRightAngle)
        backLeft.drive(wheelSpeeds[3], backLeftAngle)
        frontRight.drive(wheelSpeeds[0], frontRightAngle)
        frontLeft.drive(wheelSpeeds[1], frontLeftAngle)
    }

    fun logEncoderValues() {
        val vals = arrayOf(frontRight.getRawEncoder(), frontLeft.getRawEncoder(), backLeft.getRawEncoder(), backRight.getRawEncoder())
        SmartDashboard.putString("Encoder values", vals.joinToString(", "))
    }

    // public void resetPredictedOdometry() {
    // predictedVelocity = new Vector2(0, 0)
    // }

}
