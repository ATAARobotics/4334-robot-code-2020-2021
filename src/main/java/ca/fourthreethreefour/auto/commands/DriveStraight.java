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
   * Creates a new DriveStraight.
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
    driveSubsystem.reset();

    drivePID.getController().setTolerance(30);
    turnPID.getController().setTolerance(1);

    drivePID.setSetpoint(distance);
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
    driveSubsystem.arcadeDrive(0, 0, false);
    driveSubsystem.reset();
  }

  int i = 0;
  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
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
