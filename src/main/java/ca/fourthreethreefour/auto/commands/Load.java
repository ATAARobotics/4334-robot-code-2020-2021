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
  private Intake rollerSubsystem = null;

  private boolean cartridgeRun = false;
  /**
   * Creates a new Load.
   */
  public Load(Cartridge cartridgeSubsystem, Intake rollerSubsystem) {
    this.cartridgeSubsystem = cartridgeSubsystem;
    this.rollerSubsystem = rollerSubsystem;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    rollerSubsystem.set(1);

    if (rollerSubsystem.intakeSensor()) {
      cartridgeRun = true;
    }

    if (cartridgeRun) {
      if (cartridgeSubsystem.cartridgeEnd()) {
          if (cartridgeSubsystem.indexerSensor()) {
              cartridgeSubsystem.indexerSet(0);
              cartridgeRun = false;
          } else {
              cartridgeSubsystem.indexerSet(1);
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
    rollerSubsystem.set(0);
    cartridgeSubsystem.beltSet(0);
    cartridgeSubsystem.indexerSet(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
