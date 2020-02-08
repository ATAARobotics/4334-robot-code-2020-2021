/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour.subsystems.pid;

import com.revrobotics.CANSparkMax;

import ca.fourthreethreefour.subsystems.Shooter;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDSubsystem;

public class FlywheelPID extends PIDSubsystem {
  /**
   * Creates a new FlywheelPID.
   */
  private Shooter shooterSubsystem = null;
 
  private double speed;


  public FlywheelPID(Shooter shooterSubsystem) {
    super(new PIDController(0, 0, 0));
    this.shooterSubsystem = shooterSubsystem;
  }

  @Override
  public void useOutput(final double output, final double setpoint) {
    // Use the output here
    this.speed = output;

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
