/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour.auto.commands;

import ca.fourthreethreefour.subsystems.Intake;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class IntakeMove extends CommandBase {

  private Intake intakeSubsystem = null;
  private String direction;
  /**
   * Creates a new IntakeRaiseLower.
   */
  public IntakeMove(Intake intakeSubsystem, String direction) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.intakeSubsystem = intakeSubsystem;
    this.direction = direction;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (direction.toLowerCase().equals("up") && intakeSubsystem.intakeLimitTop()) {
      intakeSubsystem.releaseSet(-0.4);
    } else if (direction.toLowerCase().equals("down") && intakeSubsystem.intakeLimitBottom()){
      intakeSubsystem.releaseSet(0.4);
    } else {
      intakeSubsystem.releaseSet(0);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intakeSubsystem.releaseSet(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (direction.toLowerCase().equals("up")) {
      return !intakeSubsystem.intakeLimitTop();
    } else if (direction.toLowerCase().equals("down")) {
      return !intakeSubsystem.intakeLimitBottom();
    } else {
      return false;
    }
  }
}
