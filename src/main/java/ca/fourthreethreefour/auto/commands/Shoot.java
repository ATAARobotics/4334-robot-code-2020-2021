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
  double RPM;
  int shootFeed = 0;
  int indexerFeed = 0;

  /**
   * Creates a new Shoot.
   */
  public Shoot(Shooter shooterSubsystem, Cartridge cartridgeSubsystem, FlywheelPID flywheelPID, double RPM) {
    this.shooterSubsystem = shooterSubsystem;
    this.cartridgeSubsystem = cartridgeSubsystem;
    this.flywheelPID = flywheelPID;
    this.RPM = RPM;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    flywheelPID.setSetpoint(RPM);
    flywheelPID.enable();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    shooterSubsystem.flywheelSet(flywheelPID.getSpeed());

    if (flywheelPID.getController().atSetpoint()) {
      cartridgeSubsystem.indexerSet(1);
      if (!cartridgeSubsystem.indexerSensor()){
        cartridgeSubsystem.beltSet(1);
        shootFeed = 0;
      } else if (shootFeed < 3){
        shootFeed++;
        cartridgeSubsystem.beltSet(0.5);
      } 
    } else {
      if (!cartridgeSubsystem.indexerSensor()) {
        cartridgeSubsystem.beltSet(1);
        cartridgeSubsystem.indexerSet(-1);
        indexerFeed = 0;
        } else {
          cartridgeSubsystem.beltSet(0);
          if( indexerFeed < 2) {
            cartridgeSubsystem.indexerSet(0.75);
            indexerFeed++;
          } else {
            cartridgeSubsystem.indexerSet(0);
          }
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
