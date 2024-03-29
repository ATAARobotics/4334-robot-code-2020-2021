/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour.auto.commands;

import ca.fourthreethreefour.subsystems.Drive;
import ca.fourthreethreefour.subsystems.pid.TurnPID;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class Turn extends CommandBase {
  private Drive driveSubsystem = null;
  private TurnPID turnPID = null;
  private double angle;
  /**
   * Designed to turn the robot to a set angle using the NavX system.
   */
  public Turn(Drive driveSubsystem, TurnPID turnPID, double angle) {
    this.driveSubsystem = driveSubsystem;
    this.turnPID = turnPID;
    this.angle = angle;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    driveSubsystem.reset();

    turnPID.getController().setTolerance(2); // Sets the tolerance for turning.

    turnPID.setSetpoint(angle); // Sets the setpoint to the specified value.
    turnPID.enable();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    driveSubsystem.arcadeDrive(0, turnPID.getTurn(), false); // Uses the PID value
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    turnPID.disable();
    driveSubsystem.arcadeDrive(0, 0, false); // Stops the robot
    driveSubsystem.reset();
  }

  int i = 0;
  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // To help with the loops, this requires the system to stay at the setpoint for multiple counts, rather than just on first hit.
    // If it is at the setpoint, increments it by one. If it is not longer at the setpoint, it resets the count. If it passed the count, it returns true. 
    if (i < 20000) {
      if (turnPID.getController().atSetpoint()) {
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
