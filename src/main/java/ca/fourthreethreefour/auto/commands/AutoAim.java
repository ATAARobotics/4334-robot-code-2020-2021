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

public class AutoAim extends CommandBase {

  // The logic here is the same as in the Aim.java command.

  private HoodPID hoodPID = null;
  private Shooter shooterSubsystem = null;

  /**
   * Supposed to take the distance at the front of the robot, and automatically aim. Never used
   */
  public AutoAim(HoodPID hoodPID, Shooter shooterSubsystem) {
    this.hoodPID = hoodPID;
    this.shooterSubsystem = shooterSubsystem;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    hoodPID.getController().setTolerance(1);
    hoodPID.setSetpoint(shooterSubsystem.getAngleToShoot());
    hoodPID.enable();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    shooterSubsystem.shooterHoodSet(hoodPID.getSpeed());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    hoodPID.disable();
    shooterSubsystem.shooterHoodSet(0);
  }

  int i = 0;
  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
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
