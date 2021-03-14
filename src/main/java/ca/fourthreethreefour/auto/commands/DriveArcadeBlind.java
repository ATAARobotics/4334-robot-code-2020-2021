/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour.auto.commands;

import ca.fourthreethreefour.subsystems.Drive;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class DriveArcadeBlind extends CommandBase {
  private Drive driveSubsystem = null;
  private double driveSpeed;
  private double turnSpeed;
  /**
   * Drives the robot with raw speed values to each side.
   */
  public DriveArcadeBlind(Drive driveSubsystem, double driveSpeed, double turnSpeed) {
    this.driveSubsystem = driveSubsystem;
    this.driveSpeed = driveSpeed;
    this.turnSpeed = turnSpeed;

    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    driveSubsystem.arcadeDrive(driveSpeed, turnSpeed, false); // Passes in the values directly
  }

  

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    driveSubsystem.arcadeDrive(0, 0, false); // Stops it
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
