/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour.subsystems.pid;

import ca.fourthreethreefour.subsystems.Shooter;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDSubsystem;

public class HoodPID extends PIDSubsystem {
  private Shooter shooterSubsystem = null;

  private double speed;
  /**
   * Creates a new HoodPID.
   */
  public HoodPID(Shooter shooterSubsystem) {
    super(
        // The PIDController used by the subsystem
        new PIDController(0, 0, 0));
    this.shooterSubsystem = shooterSubsystem;
  }

  @Override
  public void useOutput(double output, double setpoint) {
    speed = output;
    // Use the output here
  }

  @Override
  public double getMeasurement() {
    // Return the process variable measurement here
    return shooterSubsystem.getEncoder();
  }

  public double getSpeed() {
    return speed;
  }
}
