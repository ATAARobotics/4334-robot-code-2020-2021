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
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Subsystem;

/**
 * Add your docs here.
 */
public class Intake implements Subsystem {
  private WPI_VictorSPX rollerIntake = null;
  private WPI_VictorSPX rollerRelease1 = null;
  private Lasershark lasersharkIntake = null;
  private CANSparkMax rollerRelease2 = null;
  private DigitalInput rollerReleaseLimit = null;
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  public Intake() {
    rollerIntake = new WPI_VictorSPX(Settings.ROLLER_PORT);
    rollerRelease1 = new WPI_VictorSPX(Settings.ROLLER_RELEASE_1_PORT);
    lasersharkIntake = new Lasershark(Settings.LINESHARK_INTAKE_PORT);
    rollerRelease2 = new CANSparkMax(Settings.ROLLER_RELEASE_2_PORT, MotorType.kBrushless);
    rollerReleaseLimit = new DigitalInput(Settings.ROLLER_RELEASE_LIMIT_PORT);
    rollerRelease2.setInverted(false);
  }

  public void intakeSet(double speed) {
    rollerIntake.set(speed * Settings.ROLLER_SPEED);
  }

  public void releaseSet(double speed) {
    rollerRelease1.set(speed * Settings.RELEASE_SPEED);
    rollerRelease2.set(speed * Settings.RELEASE_NEO_SPEED);
  }
  boolean hasSeen = false;
  int startLoop = 0;
  public boolean intakeSensor() {
    if ((lasersharkIntake.getDistanceInches() <= 4 && lasersharkIntake.getDistanceInches() > 0) || hasSeen) {
      hasSeen = true;
      if (startLoop <= 2){
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
  int i = 0;
  boolean hasTriggered= false;

  public boolean releaseSetLimit() {
    return rollerReleaseLimit.get();
  }
  public void runNeo(double speed){
   rollerRelease2.set(speed * Settings.RELEASE_NEO_SPEED);
  }
  public void stopVictor() {
    rollerRelease1.set(0* Settings.RELEASE_SPEED);
  }
  public void printUltrasonics() {
    System.out.println(lasersharkIntake.getDistanceInches());
  }
}
