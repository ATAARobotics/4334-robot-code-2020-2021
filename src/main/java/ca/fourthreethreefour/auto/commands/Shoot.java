/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour.auto.commands;

import ca.fourthreethreefour.subsystems.Cartridge;
import ca.fourthreethreefour.subsystems.Shooter;
import ca.fourthreethreefour.subsystems.pid.FlywheelPID;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class Shoot extends CommandBase {
  Shooter shooterSubsystem = null;
  Cartridge cartridgeSubsystem = null;
  FlywheelPID flywheelPID = null;

  /**
   * Creates a new Shoot.
   */
  public Shoot(Shooter shooterSubsystem, Cartridge cartridgeSubsystem, FlywheelPID flywheelPID) {
    this.shooterSubsystem = shooterSubsystem;
    this.cartridgeSubsystem = cartridgeSubsystem;
    this.flywheelPID = flywheelPID;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    flywheelPID.enable();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    shooterSubsystem.flywheelSet(flywheelPID.getSpeed());

    if (flywheelPID.getController().atSetpoint()) {
      cartridgeSubsystem.indexerSet(1);
      if (!cartridgeSubsystem.indexerSensor()) {
        cartridgeSubsystem.beltSet(1);
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    flywheelPID.disable();
    cartridgeSubsystem.indexerSet(0);
    cartridgeSubsystem.beltSet(0);
    shooterSubsystem.flywheelSet(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
