/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour.auto.commands;

import ca.fourthreethreefour.subsystems.Drive;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class DriveBlind extends CommandBase {
  private Drive driveSubsystem = null;
  private double leftSpeed;
  private double rightSpeed;
  /**
   * Drives the robot with raw speed values to each side.
   */
  public DriveBlind(Drive driveSubsystem, double leftSpeed, double rightSpeed) {
    this.driveSubsystem = driveSubsystem;
    this.leftSpeed = leftSpeed;
    this.rightSpeed = rightSpeed;

    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    driveSubsystem.tankDrive(leftSpeed, rightSpeed); // Passes in the values directly.
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    driveSubsystem.tankDrive(0, 0); // Stops it
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
