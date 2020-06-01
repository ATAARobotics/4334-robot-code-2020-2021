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
   * Takes a specified direction (up/down) and moves the intake to those preset positions.
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
    if (direction.toLowerCase().equals("up") && intakeSubsystem.intakeLimitTop()) { // If its told to go up, and the top limitswitch isn't pressed.
      intakeSubsystem.releaseSet(-0.4); // Moves upwards slowly
    } else if (direction.toLowerCase().equals("down") && intakeSubsystem.intakeLimitBottom()) { // If told to go down, and the bottom limitswitch isn't pressed.
      intakeSubsystem.releaseSet(0.4); // Moves downwards slowly
    } else { 
      intakeSubsystem.releaseSet(0); // Ensures that it stops
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
      return !intakeSubsystem.intakeLimitTop(); // If its told to go up, gets the top limitswitch state (false being that its pressed)
    } else if (direction.toLowerCase().equals("down")) {
      return !intakeSubsystem.intakeLimitBottom(); // If its told to go down, gets the bottom limitswitch state
    } else {
      return false;
    }
  }
}
