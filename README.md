# SwoLib

FRC team 2875 Cyberhawks open source Java/Kotlin library. The library is written in Kotlin; however, thanks to Kotlin's perfect Java interoperation, it can be used (without any extra effort) in Java projects as well. 

## Quick Links:
 * [Docs](https://cshcyberhawks.github.io/SwoLib/)
 * [Pathplanner](https://github.com/CshCyberhawks/pathplanner)
 * [Examples](https://github.com/CshCyberhawks/SwoLib/tree/main/Examples/SwerveBot)
 * [Dynamic Autonomous Demonstration](https://drive.google.com/file/d/1a_YoA8lqkqwJuUBnri_qyLccZbWwAqEM/view?usp=sharing)

## Getting Started:

### Installation

Instruction on how to add this library to your project can be found [here](https://jitpack.io/#CshCyberhawks/SwoLib).

### Documentation:
The library is extensively documented with Dokka, a Kotlin-based improved version of JavaDoc. This is hosted [here](https://cshcyberhawks.github.io/SwoLib/) by GitHub Pages. An example demonstrating many of the libraries swerve drive related capabilities can also be found [here](https://github.com/CshCyberhawks/SwoLib/tree/main/Examples/SwerveBot). This example exists in both Java and Kotlin. 


## Features:

- [Custom swerve drive utilities](#swerve-drive)
- [Math utilities](#Math)
- [Motor utilities](#Motors)
- [Autnomous utilities](#autonomous)
- [Limelight utilities](#Limelight)
- [Various quality of life utilities](#QOL)


### Swerve-drive:

- None of the swerve-drive utilities use the wpilib swerve drive classes, they are all completely custom
- Contains a class to wrap the entire swerve drive (swerve drive train)
- Classes for each individual swerve module (swervewheel).
- Custom odometry - takes in encoder data from each individual wheel and calculates robot position. This is more accurate than an accelerometer, however, cannot detect any external stresses (ie driving into a wall) and thus is recommended to be used solely in autonomous. In 2023's game, it is possible to correct this through the use of limelights and AprilTags. SwoLib has the capability to do this, if you choose. 
- Wheel speed normalization
- PID based drift correction for both positional and angular drift (using a NavX gyro and our custom position tracking odometry)
- Integration with Field2d widget to display robot position on the field in Shuffleboard
- Automatic resetting of odometry with Limelight and AprilTag fiducials

### Autonomous:

#### FULLY DYNAMIC!! - The robot is able to start anywhere on the field and (as long as it can see an april tag with a limelight) can discern its starting position. This means that the robot will automatically correct itself to the first waypoint of the path, regardless of where it starts. This is made possible by our custom swerve drive autonomous utilities which perform most calculations at runtime, in contrast the WPILib where motion profiling is done ahead of time. 2875 uses SwoLib's autonomous classes in teleop to perform automatic lineups for placement of cones/cubes and is able to engage this from anywhere on the field! In [this video](https://drive.google.com/file/d/1a_YoA8lqkqwJuUBnri_qyLccZbWwAqEM/view?usp=sharing), every time the robot drives towards the grid, that is the SwoLib autonomous taking complete control and going to the exact same position (the position was set from our pathplanner). This is done by a `GoToPosition` with a preset desired end location bound to buttons on our joystick. PS: please forgive the poorly tuned PIDs in the video.

- Autonomous swerve drive utilities - custom written - doesn't use any wpilib trajectory generation or swerve classes
- High and lower level classes - ranging from commands to go to a cerain coordinate to the class that manipulates the swerve drive train
- PID auto corrected (if the robot drifts off course, it will corerct itself using a PID), and trapezoid motion profiled (gradual acceleration and deceleration so as to not overshoot targets)
- Turning controlled with PID
- Several commands to easily move the robot to positions
- Limelight integration - use the limelight to track targets (ie balls) and feed positional data for the robot to move to.
- trajectory generation - through the chaining of go-to-position commands, the robot can be made to follow a set path while still self-correcting
- [Modified pathplanner](https://github.com/CshCyberhawks/pathplanner) this can be used to draw robot paths and link them together.

### Limelight:
 - Limelight wrapper class with null-safety through Kotlin optionals.
 - Acess to all network-tables limelight values with easy-to-use methods.
 - Integration throughout the library - dynamic amount of limelights (you can use multiple limelights to reset odometry)
 - Automatic tracking of objects in 3d space using limelight horizontal and vertical offsets.
 - Autonomous commands to move towards objects - motion profiled and PID corrected.
 - Easily manage any number of limelights.
 
### Motors:
 - Library support for various types of motor controllers ranging from Falcon500's TalonFX to the CANSparkMax
 - Wrapper classes for encoders
 
### Math:
 - Various coordinate utilities. These include classes for polar and cartesian coordinates with overloaded operators.
 - Vector2, Vector3, Vector2 + Angle (Fieldposition) classes
 - Weighted deadzoning for controllers
 - Normalization utilities
 - Bezier curve utilities
 
### QOL:
 - Wrapper interface for gyros (NavX and Pigeon). This ensures they all behave the same way
 - All major classes (`SwerveDriveTrain`, `SwerveAuto`, `SwerveOdometry`, etc) have optional `debugLogging` parameters, which (when set to true) will create a Shuffleboard tab for that class containing useful debugging information. 

## Visualization
![Visualization of this repo](./diagram.svg)