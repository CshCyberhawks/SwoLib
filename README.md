# SwoLib
FRC team 2875 Cyberhawks open source kotlin library (it is usable from Java as well)

## Features:
 * [Custom swerve drive utilities](#swerve-drive)
 * [Math utilities](Math)
 * [Motor utilities](Motors)

## Planned features:
 * [Autnomous utilities](#autonomous)
 * [Limelight utilities](Limelight)
 * [Various quality of life utilities](QOL)


### Swerve-drive:
 * None of the swerve-drive utilities use the wpilib swerve drive classes, they are all completely custom
 * Contains a class to wrap the entire swerve drive (swerve drive train)
 * Classes for each individual swerve module (swervewheel). Note: these are currently configured for swerve modules with a single falcon 500 motor (drive) and a single 775 motor (turn). It is also expected to have an analog encoder for wheel turning data
 * Custom odometry - takes in encoder data from each individual wheel and calculates robot position. This is more accurate than an accelerometer, however, cannot detect any external stresses (ie driving into a wall) and thus is recommended to be used solely in autonomous. 
 * Wheel speed normalization
 * PID based drift correction for both positional and angular drift (using a NavX gyro and our custom position tracking odometry)

### Autonomous:
 * Autonomous swerve drive utilities - custom written - doesn't use any wpilib trajectory generation or swerve classes
 * High and lower level classes - ranging from commands to go to a cerain coordinate to the class that manipulates the swerve drive train
 * PID auto corrected (if the robot drifts off course, it will corerct itself using a PID), and trapezoid motion profiled (gradual acceleration and deceleration so as to not overshoot targets)
 * Turning controlled with PID
 * Several commands to easily move the robot to positions
 * Limelight integration - use the limelight to track targets (ie balls) and feed positional data for the robot to move to. 
 * trajectory generation - through the chaining of go-to-position commands, the robot can be made to follow a set path while still self-correcting
