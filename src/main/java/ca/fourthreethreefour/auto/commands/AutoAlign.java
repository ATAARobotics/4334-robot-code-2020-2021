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

  public AutoAlign(AlignPID alignPID, Drive driveSubsystem) {
    this.alignPID = alignPID;
    this.driveSubsystem = driveSubsystem;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    driveSubsystem.reset();
    alignPID.getController().setTolerance(2);
    alignPID.setSetpoint(0.0);
    alignPID.enable();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    driveSubsystem.arcadeDrive(0, alignPID.getRotateSpeed(), false);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    alignPID.disable();
    driveSubsystem.arcadeDrive(0, 0, false);
    driveSubsystem.reset();
  }

  int i = 0;
  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
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
}
