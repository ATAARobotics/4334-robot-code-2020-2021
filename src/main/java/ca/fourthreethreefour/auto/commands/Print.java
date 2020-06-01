/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour.auto.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class Print extends InstantCommand {
  private String string = null;
  /**
  * For debugging purposes, prints a string.
  */
  public Print(String string) {
    this.string = string;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println(string);
  }
}
