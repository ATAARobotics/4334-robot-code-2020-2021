/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Add your docs here.
 */
public class Cartridge extends Subsystem {
  private WPI_TalonSRX innerBelt = null;
  private WPI_TalonSRX outerBelt = null;
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  public Cartridge() {
    innerBelt = new WPI_TalonSRX(0);
    outerBelt = new WPI_TalonSRX(1);
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
