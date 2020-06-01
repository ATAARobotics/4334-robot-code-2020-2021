/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour.auto.commands;

import ca.fourthreethreefour.subsystems.Drive;
import ca.fourthreethreefour.subsystems.pid.DrivePID;
import ca.fourthreethreefour.subsystems.pid.TurnPID;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class DriveStraight extends CommandBase {
  private Drive driveSubsystem = null;
  private DrivePID drivePID = null;
  private TurnPID turnPID = null;
  private double distance;
  /**
   * Drives the robot to a set distance, given in inches, and stops at the point.
   */
  public DriveStraight(Drive driveSubsystem, DrivePID drivePID, TurnPID turnPID, double distance) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.driveSubsystem = driveSubsystem;
    this.drivePID = drivePID;
    this.turnPID = turnPID;
    this.distance = distance;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    driveSubsystem.reset(); // Resets the encoders

    drivePID.getController().setTolerance(30); // How many inches and degrees we want it to be around on
    turnPID.getController().setTolerance(1); // The smaller the value, the harder for the encoders to reach.

    drivePID.setSetpoint(distance); // Sets both as the setpoints
    turnPID.setSetpoint(driveSubsystem.getNavX());

    drivePID.enable(); // Enables both
    turnPID.enable();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    driveSubsystem.arcadeDrive(drivePID.getSpeed(), turnPID.getTurn(), false); // Uses the returned values from the PID directly.
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivePID.disable();
    turnPID.disable(); // Make sure to turn everything off afterwards, even if it is just numbers.
    driveSubsystem.arcadeDrive(0, 0, false);
    driveSubsystem.reset();
  }

  int i = 0;
  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // To help with the loops, this requires the system to stay at the setpoint for multiple counts, rather than just on first hit.
    // If it is at the setpoint, increments it by one. If it is not longer at the setpoint, it resets the count. If it passed the count, it returns true. 
    if (i < 20000) {
      if (drivePID.getController().atSetpoint()) {
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
