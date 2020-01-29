/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import ca.fourthreethreefour.settings.Settings;
import edu.wpi.first.wpilibj2.command.Subsystem;

/**
 * Add your docs here.
 */
public class Climb implements Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  private WPI_TalonSRX releaseMotor = null;

  public Climb() {
    releaseMotor = new WPI_TalonSRX(Settings.CLIMB_RELEASE_PORT);
  }

  public void releaseSet(double speed) {
    releaseMotor.set(speed);
  }
}
