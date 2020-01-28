/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import ca.fourthreethreefour.settings.Settings;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * Add your docs here.
 */
public class Drive extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  private CANSparkMax leftBackMotor = null;
  private CANSparkMax rightFrontMotor = null;
  private CANSparkMax rightBackMotor = null;

  private SpeedControllerGroup leftMotors = null;
  private SpeedControllerGroup rightMotors = null;

  private DifferentialDrive drive = null;

  public Drive() {
    leftFrontMotor = new CANSparkMax(Settings.LEFT_FRONT_MOTOR_PORT, MotorType.kBrushless);
    leftBackMotor = new CANSparkMax(Settings.LEFT_BACK_MOTOR_PORT, MotorType.kBrushless);
    rightFrontMotor = new CANSparkMax(Settings.RIGHT_FRONT_MOTOR_PORT, MotorType.kBrushless);
    rightBackMotor = new CANSparkMax(Settings.RIGHT_BACK_MOTOR_PORT, MotorType.kBrushless);

    leftMotors = new SpeedControllerGroup(leftFrontMotor, leftBackMotor);
    rightMotors = new SpeedControllerGroup(rightFrontMotor, rightBackMotor);

    drive = new DifferentialDrive(leftMotors, rightMotors);
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }

  public void teleopInit() {
    drive.setSafetyEnabled(true);
    drive.setExpiration(0.5);
  }

  public void arcadeDrive(double speed, double turn, boolean squared) {
    drive.arcadeDrive(speed, turn, squared);
  }
}
