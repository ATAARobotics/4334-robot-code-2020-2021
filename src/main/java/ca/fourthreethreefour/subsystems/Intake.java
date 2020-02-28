/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.cuforge.libcu.Lasershark;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import ca.fourthreethreefour.settings.Settings;
import edu.wpi.first.wpilibj2.command.Subsystem;

/**
 * Add your docs here.
 */
public class Intake implements Subsystem {
  private WPI_VictorSPX rollerIntake = null;
  private WPI_VictorSPX rollerRelease1 = null;
  private Lasershark lasersharkIntake = null;
  private CANSparkMax rollerRelease2 = null;
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  public Intake() {
    rollerIntake = new WPI_VictorSPX(Settings.ROLLER_PORT);
    rollerRelease1 = new WPI_VictorSPX(Settings.ROLLER_RELEASE_1_PORT);
    lasersharkIntake = new Lasershark(Settings.LINESHARK_INTAKE_PORT);
    rollerRelease2 = new CANSparkMax(Settings.ROLLER_RELEASE_2_PORT, MotorType.kBrushless);
    rollerRelease2.setInverted(true);
  }

  public void intakeSet(double speed) {
    rollerIntake.set(speed * Settings.ROLLER_SPEED);
  }

  public void releaseSet(double speed) {
    rollerRelease1.set(speed * Settings.RELEASE_SPEED);
    rollerRelease2.set(speed * Settings.RELEASE_SPEED);
  }
  boolean hasSeen = false;
  int startLoop = 0;
  public boolean intakeSensor() {
    if (lasersharkIntake.getDistanceInches() <= 4 || hasSeen) {
      hasSeen = true;
      if (startLoop <= 50){
        startLoop++;
        return false;
      } else {
        hasSeen = false;
        startLoop = 0;
        return true;
      } 
    } else {
      return false;
    }
  }
}
