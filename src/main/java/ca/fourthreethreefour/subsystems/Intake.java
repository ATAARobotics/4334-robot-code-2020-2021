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

import ca.fourthreethreefour.logging.Logging;
import ca.fourthreethreefour.settings.Settings;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Subsystem;

/**
 * Add your docs here.
 */
public class Intake implements Subsystem {
  private WPI_VictorSPX intakeIntake = null;
  private WPI_VictorSPX intakeRelease1 = null;
  private Lasershark lasersharkIntake = null;
  private CANSparkMax intakeRelease2 = null;
  private DigitalInput intakeLimitTop = null;
  private DigitalInput intakeLimitBottom = null;
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  public Intake() {
    intakeIntake = new WPI_VictorSPX(Settings.INTAKE_PORT);
    intakeRelease1 = new WPI_VictorSPX(Settings.INTAKE_RELEASE_1_PORT);
    lasersharkIntake = new Lasershark(Settings.LINESHARK_INTAKE_PORT);
    intakeRelease2 = new CANSparkMax(Settings.INTAKE_RELEASE_2_PORT, MotorType.kBrushless);
    intakeLimitTop = new DigitalInput(Settings.INTAKE_LIMIT_TOP_PORT);
    intakeLimitBottom = new DigitalInput(Settings.INTAKE_LIMIT_BOTTOM_PORT);
    intakeRelease2.setInverted(true);
    intakeRelease1.setInverted(true);
  }

  public void intakeSet(double speed) {
    intakeIntake.set(speed * Settings.INTAKE_SPEED);
  }

  public void releaseSet(double speed) {
    intakeRelease1.set(speed * Settings.RELEASE_SPEED);
    intakeRelease2.set(speed * Settings.RELEASE_NEO_SPEED);
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

  public boolean intakeLimitTop() {
    return intakeLimitTop.get();
  }
  public boolean intakeLimitBottom() {
    return intakeLimitBottom.get();
  }
  public void runNeo(double speed){
   intakeRelease2.set(speed * Settings.RELEASE_NEO_SPEED);
  }
  public void stopVictor() {
    intakeRelease1.set(0* Settings.RELEASE_SPEED);
  }
  public void printUltrasonics() {
    Logging.put("Intake Sensor", lasersharkIntake.getDistanceInches());
  }
}
