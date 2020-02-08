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

import ca.fourthreethreefour.settings.Settings;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj.CAN;
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

    drive = new DifferentialDrive(leftMotors, rightMotors);
    
    leftMotors.setInverted(true);
    rightMotors.setInverted(true);

    try {
      navX = new AHRS(SPI.Port.kMXP);
    } catch (Exception e) {
      DriverStation.reportError("Error instantiating navX MXP:  " + e.getMessage(), true);
    }
    
    leftEncoder = new CANCoder(Settings.LEFT_ENCODER_PORT);
    rightEncoder = new CANCoder(Settings.RIGHT_ENCODER_PORT);
  }

  public void teleopInit() {
    drive.setSafetyEnabled(true);
    drive.setExpiration(0.5);
  }

  public void arcadeDrive(double speed, double turn, boolean squared) {
    drive.arcadeDrive(speed * speedModifier, turn * Settings.TURN_SPEED, squared);
  }
  
  public void tankDrive(double leftSpeed, double rightSpeed) {
    drive.tankDrive(leftSpeed, rightSpeed);
  }

  public double getNavX() {
    return navX.getAngle();
  } 

  public double getLeftEncoder() {
    return leftEncoder.getPosition();
  }
  public double getRightEncoder() {
    return rightEncoder.getPosition();    
  }
  public double getEncoder() {
    double meanEncoder = getLeftEncoder() + getRightEncoder();
    meanEncoder = meanEncoder/2;
    return meanEncoder;
  }

  public void reset() {
    navX.reset();
    leftEncoder.setPosition(0);
    rightEncoder.setPosition(0);
  }

  public static String loggingCategories() {
    String categories = "";
    return categories;
  }

  public static String loggingData() {
    String data = "";
    return data;
  }

  public void speedHigh() {
    speedModifier = Settings.DRIVE_MAX_SPEED;
  }

  public void speedLow() {
    speedModifier = Settings.DRIVE_SPEED;
  }
}

