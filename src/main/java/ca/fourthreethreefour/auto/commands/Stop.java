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

public class Stop extends CommandBase {
  Drive driveSubsystem = null;
  DrivePID drivePID = null;
  TurnPID turnPID = null;
  /**
   * Creates a new Stop.
   */
  public Stop(Drive driveSubsystem, DrivePID drivePID, TurnPID turnPID) {
    this.driveSubsystem = driveSubsystem;
    this.drivePID = drivePID;
    this.turnPID = turnPID;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    driveSubsystem.reset();

    drivePID.getController().setTolerance(10);
    turnPID.getController().setTolerance(1);

    drivePID.setSetpoint(0);
    turnPID.setSetpoint(driveSubsystem.getNavX());

    drivePID.enable();
    turnPID.enable();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    driveSubsystem.arcadeDrive(drivePID.getSpeed(), turnPID.getTurn(), false);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivePID.disable();
    turnPID.disable();
    driveSubsystem.reset();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
