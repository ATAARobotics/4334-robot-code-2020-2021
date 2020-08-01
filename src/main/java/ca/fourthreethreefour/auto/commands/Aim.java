/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour.auto.commands;

import ca.fourthreethreefour.subsystems.Shooter;
import ca.fourthreethreefour.subsystems.pid.HoodPID;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class Aim extends CommandBase {

  private HoodPID hoodPID = null;
  private Shooter shooterSubsystem = null;
  private double angle;

  /**
   * Takes an angle, and moves the hood to that setpoint.
   */
  public Aim(HoodPID hoodPID, Shooter shooterSubsystem, double angle) {
    this.hoodPID = hoodPID;
    this.shooterSubsystem = shooterSubsystem;
    this.angle = angle;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    hoodPID.getController().setTolerance(1); // For the hood, we want it to be within 1 degree of accuracy. Smaller is better, but too small makes its too hard for it to stop.
    hoodPID.setSetpoint(angle); // Sets the angle to go for.
    hoodPID.enable(); // Activates
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    shooterSubsystem.shooterHoodSet(hoodPID.getSpeed()); // Takes the output from the hoodPID, and passes it to the actual set command.
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    hoodPID.disable(); // Makes sure that the PID has stopped. Even though it only gives a value, it still is better both for memory and general safety.
    shooterSubsystem.shooterHoodSet(0); // Tells the motor to actually stop running, it requires a 0 value to actually stop.
  }

  int i = 0;
  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // To help with the loops, this requires the hood to stay at the setpoint for multiple counts, rather than just on first hit.
    // If it is at the setpoint, increments it by one. If it is not longer at the setpoint, it resets the count. If it passed the count, it returns true. 
    if (i < 20000) {
      if (hoodPID.getController().atSetpoint()) {
        i++;
        return false;
      } else {
        i = 0;
        return false;
      }
    } else {
      return true;
    }
  }
}
