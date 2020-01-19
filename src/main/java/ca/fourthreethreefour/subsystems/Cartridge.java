/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import ca.fourthreethreefour.settings.Settings;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Add your docs here.
 */
public class Cartridge extends Subsystem {
  private WPI_TalonSRX innerBelt = null;
  private WPI_TalonSRX outerBelt = null;
  private WPI_TalonSRX indexer = null;
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  public Cartridge() {
    innerBelt = new WPI_TalonSRX(Settings.INNER_BELT_PORT);
    outerBelt = new WPI_TalonSRX(Settings.OUTER_BELT_PORT);
    indexer = new WPI_TalonSRX(Settings.INDEXER_PORT);
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
  public void innerSet(double speed) {
    innerBelt.set(speed);
  }
  public void outerSet(double speed) {
    outerBelt.set(speed);
  }
  public void indexerSet(double speed) {
    indexer.set(speed);
  }

  public boolean indexerSensor() {
    return true;

  }

  public boolean cartridgeStart() {
    return true;

  }

  public boolean cartridgeEnd() {
    return true;

  }
}
