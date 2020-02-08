/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.cuforge.libcu.Lasershark;

import ca.fourthreethreefour.settings.Settings;
import edu.wpi.first.wpilibj2.command.Subsystem;

/**
 * Add your docs here.
 */
public class Intake implements Subsystem {
  private WPI_TalonSRX rollerIntake = null;
  private Lasershark lasersharkIntake = null;
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  public Intake() {
    rollerIntake = new WPI_TalonSRX(Settings.ROLLER_PORT);
    lasersharkIntake = new Lasershark(Settings.LINESHARK_INTAKE_PORT);
  }

  public void set(double speed) {
    rollerIntake.set(speed * Settings.ROLLER_SPEED);
  }

  public boolean intakeSensor() {
    if (lasersharkIntake.getDistanceInches() <= 7) {
      return true;
    } else {
      return false;
    }
  }
}
