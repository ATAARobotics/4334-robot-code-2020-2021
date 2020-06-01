/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour.auto.commands;

import ca.fourthreethreefour.subsystems.Cartridge;
import ca.fourthreethreefour.subsystems.Intake;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class Load extends CommandBase {
  private Cartridge cartridgeSubsystem = null;
  private Intake intakeSubsystem = null;

  private boolean cartridgeRun = false;
  int indexerFeed = 0;
  int cartridgeTime = 0;
  /**
   * Designed to load the robot, to be ran alongside other commands such as moving. The logic here is exactly like the same code in Teleop.java, check there for comments.
   */
  public Load(Cartridge cartridgeSubsystem, Intake intakeSubsystem) {
    this.cartridgeSubsystem = cartridgeSubsystem;
    this.intakeSubsystem = intakeSubsystem;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    intakeSubsystem.intakeSet(1);

    if (intakeSubsystem.intakeSensor()) {
      cartridgeRun = true;
    }

    if (cartridgeRun) {
      if (cartridgeTime < 20) {
          cartridgeTime++;
      } else {
          cartridgeRun = false;
      }
    } else {
      cartridgeTime = 0;
    }

    if (cartridgeRun) {
      if (cartridgeSubsystem.indexerSensor()) {
        if( indexerFeed < 5) {
            cartridgeSubsystem.indexerSet(0.75);
            indexerFeed++;
        } else {
            cartridgeSubsystem.indexerSet(0);
        }
      } else {
        indexerFeed = 0;
        cartridgeSubsystem.indexerSet(-1);
      }
      if (cartridgeSubsystem.cartridgeEnd()) {
        if (cartridgeSubsystem.indexerSensor()) {
            cartridgeRun = false;
        } else {
            if (!cartridgeSubsystem.cartridgeStart()) {
                cartridgeSubsystem.beltSet(1);
            } else {
                cartridgeSubsystem.beltSet(0);
            }
          }
      } else {
        if (!cartridgeSubsystem.cartridgeStart()) {
            cartridgeSubsystem.beltSet(1);
        } else {
            cartridgeSubsystem.beltSet(0);
            cartridgeRun = false;
        }
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intakeSubsystem.intakeSet(0);
    cartridgeSubsystem.beltSet(0);
    cartridgeSubsystem.indexerSet(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
