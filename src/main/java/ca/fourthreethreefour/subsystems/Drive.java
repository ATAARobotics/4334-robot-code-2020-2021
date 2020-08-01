/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour.subsystems;

import com.ctre.phoenix.sensors.CANCoder;
import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import ca.fourthreethreefour.logging.Logging;
import ca.fourthreethreefour.settings.Settings;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;

/**
 * Add your docs here.
 */
public class Drive implements Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  private CANSparkMax leftFrontMotor = null;
  private CANSparkMax leftBackMotor = null;
  private CANSparkMax rightFrontMotor = null;
  private CANSparkMax rightBackMotor = null;

  private SpeedControllerGroup leftMotors = null;
  private SpeedControllerGroup rightMotors = null;

  private DifferentialDrive drive = null;

  private AHRS navX = null;

  private double speedModifier = Settings.DRIVE_SPEED;
  private CANCoder leftEncoder = null;
  private CANCoder rightEncoder = null;

  public Drive() {
    leftFrontMotor = new CANSparkMax(Settings.LEFT_FRONT_MOTOR_PORT, MotorType.kBrushless);
    leftBackMotor = new CANSparkMax(Settings.LEFT_BACK_MOTOR_PORT, MotorType.kBrushless);
    rightFrontMotor = new CANSparkMax(Settings.RIGHT_FRONT_MOTOR_PORT, MotorType.kBrushless);
    rightBackMotor = new CANSparkMax(Settings.RIGHT_BACK_MOTOR_PORT, MotorType.kBrushless);

    leftMotors = new SpeedControllerGroup(leftFrontMotor, leftBackMotor);
    rightMotors = new SpeedControllerGroup(rightFrontMotor, rightBackMotor);

    drive = new DifferentialDrive(leftMotors, rightMotors); // Groups all the motors together into one drive object
    
    // So that + is one direction and - is another direction, we invert the motors so that its always consistent through all code usage.
    leftMotors.setInverted(false);
    rightMotors.setInverted(false);

    // Sets up the navX. Has to be done in a try catch, as the navX is a finicky piece of hardware.
    try {
      navX = new AHRS(SPI.Port.kMXP);
    } catch (Exception e) {
      DriverStation.reportError("Error instantiating navX MXP:  " + e.getMessage(), true);
    }
    
    leftEncoder = new CANCoder(Settings.LEFT_ENCODER_PORT);
    rightEncoder = new CANCoder(Settings.RIGHT_ENCODER_PORT);
  }

  /**
   * Sets up the important settings on the drive system. Safety Enabled is needed for teleop so that if there is no input, it stops.
   * Unlike in Auto where the motor is constantly being given values. Expiration is set alongside it to specify how soon after no input does it stop. 
   */
  public void teleopInit() {
    drive.setSafetyEnabled(true);
    drive.setExpiration(0.5);
  }

  /**
   * Runs the arcadeDrive function of the drive object.
   * @param speed - the drive speed multiplied by the speed modifier (high speed, low speed, default speed). [-1.0 - 1.0]
   * @param turn - the rotation speed multiplied by the turn modifier. [-1.0 - 1.0]
   * @param squared - specifies if the values are to be squared. useful for more natural feeling stick control but less useful when passing direct values.
   */
  public void arcadeDrive(double speed, double turn, boolean squared) {
    drive.arcadeDrive(speed * speedModifier, turn * Settings.TURN_SPEED, squared);
  }
  
  /**
   * Runs the tankDrive function of the drive object
   * @param leftSpeed - the drive speed of the left motors. [-1.0 - 1.0]
   * @param rightSpeed - the drive speed of the right motors. [-1.0 - 1.0]
   */
  public void tankDrive(double leftSpeed, double rightSpeed) {
    drive.tankDrive(leftSpeed, rightSpeed);
  }

  /**
   * The current angle of the navX relative to last reset.
   * @return angle of the NavX - inversed for consistency with other mechanisms, in degrees.
   */
  public double getNavX() {
    return -navX.getAngle(); //getPitch is returning with value 0.0
  } 

  public void printEverything() {
    Logging.put("Angle", navX.getAngle());
    Logging.put("Pitch", navX.getPitch());
    Logging.put("Yaw", navX.getYaw());
    Logging.put("Roll", navX.getRoll());
  }

  /**
   * The current distance of the left side CANCoder.
   * @return distance of the left side wheels - modified so to return the value in inches.
   */
  public double getLeftEncoder() {
    return leftEncoder.getPosition() / 4096 * 25; // [4096 is the total ticks of one full rotation. 25 is the inch diameter of the wheel]
  }

  /**
   * The current distance of the right side CANCoder.
   * @return distance of the right side wheels - modified so to return the value in inches.
   */
  public double getRightEncoder() {
    return rightEncoder.getPosition() / 4096 * 25; // [4096 is the total ticks of one full rotation. 25 is the inch diameter of the wheel]
  }

  /**
   * The current velocity of the left side CANCoder.
   * @return velocity of the left side wheels
   */
  public double getLeftVelocity() {
    return leftEncoder.getVelocity();
  }

  /**
   * The current velocity of the right side CANCoder.
   * @return velocity of the right side wheels
   */
  public double getRightVelocity() {
    return rightEncoder.getVelocity();
  }

  /**
   * The average distance of both encoder distances. Make sure both are using the same sign for positive and negative else it will not return properly.
   * @return distance the entire robot travelled.
   */
  public double getEncoder() {
    double meanEncoder = getLeftEncoder() + getRightEncoder();
    meanEncoder = meanEncoder/2;
    return meanEncoder;
  }

  /**
   * The average velocity of both encoder velocities. Make sure both are using the same sign for positive and negative else it will not return properly.
   * @return velocity of the robot
   */
  public double getVelocity() {
    double meanVelocity = getLeftVelocity() + getRightVelocity();
    meanVelocity = meanVelocity/2;
    return meanVelocity;
  }

  /**
   * Resets the values of the encoders.
   * Uses the navX built in function, sets the encoder position to 0 as to practically reset the values.
   */
  public void reset() {
    navX.reset();
    leftEncoder.setPosition(0);
    rightEncoder.setPosition(0);
  }

  /** @deprecated */
  public static String loggingCategories() {
    String categories = "";
    return categories;
  }

  /** @deprecated */
  public static String loggingData() {
    String data = "";
    return data;
  }

  /**
   * Sets the speedModifier to the high speed setting
   */
  public void speedHigh() {
    speedModifier = Settings.DRIVE_MAX_SPEED;
  }

  /**
   * Sets the speedModifier to the low speed setting
   */
  public void speedLow() {
    speedModifier = Settings.DRIVE_SPEED;
  }
}

