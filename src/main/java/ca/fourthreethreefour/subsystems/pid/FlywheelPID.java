/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour.subsystems.pid;

import com.revrobotics.CANSparkMax;

import ca.fourthreethreefour.settings.Settings;
import ca.fourthreethreefour.subsystems.Shooter;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj2.command.PIDSubsystem;
import edu.wpi.first.wpiutil.math.MathUtil;

public class FlywheelPID extends PIDSubsystem {
  /**
   * Creates a new FlywheelPID.
   */
  private Shooter shooterSubsystem = null;
 
  private double speed;


  public FlywheelPID(Shooter shooterSubsystem) {
    super(new PIDController(0.006, 0, 0));
    
    this.shooterSubsystem = shooterSubsystem;
    getController().setTolerance(10);
  }

  @Override
  public void useOutput(double output, double setpoint) {
    // Use the output here
    speed = MathUtil.clamp(output, 0, 0.5) + 0.5;

  }

  @Override
  public double getMeasurement() {
    // Return the process variable measurement here
    return shooterSubsystem.getRPM();
  }
  public double getSpeed() {

    return speed;

  }
}
