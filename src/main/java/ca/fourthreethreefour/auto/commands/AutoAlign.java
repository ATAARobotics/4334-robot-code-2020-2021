/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour.auto.commands;

import ca.fourthreethreefour.subsystems.Drive;
import ca.fourthreethreefour.subsystems.pid.AlignPID;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class AutoAlign extends CommandBase {

  private AlignPID alignPID;
  private Drive driveSubsystem;

  /**
   * Using the targets as reference, uses limelight values to turn the robot towards the targets seen.
   */
  public AutoAlign(AlignPID alignPID, Drive driveSubsystem) {
    this.alignPID = alignPID;
    this.driveSubsystem = driveSubsystem;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    driveSubsystem.reset(); // Resets encoders
    alignPID.getController().setTolerance(2); // Sets the degrees needed.
    alignPID.setSetpoint(0.0); // For limelight, we want it to be a value of 0.
    alignPID.enable();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    driveSubsystem.arcadeDrive(0, alignPID.getRotateSpeed(), false); // Uses the speed determined by the PID to turn.
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    alignPID.disable(); // Make sure to disable the PID
    driveSubsystem.arcadeDrive(0, 0, false); // And give it a value to stop.
    driveSubsystem.reset();
  }

  int i = 0;
  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // To help with the loops, this requires the system to stay at the setpoint for multiple counts, rather than just on first hit.
    // If it is at the setpoint, increments it by one. If it is not longer at the setpoint, it resets the count. If it passed the count, it returns true. 
    if (i < 20000) {
      if (alignPID.getController().atSetpoint()) {
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
