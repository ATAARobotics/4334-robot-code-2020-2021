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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
    
    // So that + is one direction and - is another direction, we invert the motors so that its always consistent through all code usage.
    intakeRelease2.setInverted(true);
    intakeRelease1.setInverted(true);
  }

  /**
   * Sets the speed of the intake
   * @param speed - the value it should run at. Is multiplied by the setting.
   */
  public void intakeSet(double speed) {
    intakeIntake.set(speed * Settings.INTAKE_SPEED);
  }

  /**
   * Sets the speed of both intake release motors
   * @param speed - the value it should run at. Is multiplied by the settings.
   */
  public void releaseSet(double speed) {
    intakeRelease1.set(speed * Settings.RELEASE_SPEED);
    intakeRelease2.set(speed * Settings.RELEASE_NEO_SPEED);
  }

  boolean hasSeen = false;
  int startLoop = 0;
  /**
   * Checks to see if the intake sensor detects a ball.
   * A sensitive part of the robot, as it is constantly moving. Prone to false positives, which is an issue.
   * @return true if sensor detects a ball.
   */
  public boolean intakeSensor() {
    // If the ball is less than 3 and a half inches away, but not 0 (0 means either disconnect or nothing at all being seen) or hasSeen flag has been set
    if ((lasersharkIntake.getDistanceInches() <= 3.5 && lasersharkIntake.getDistanceInches() > 0) || hasSeen) {
      // Sets the flag
      hasSeen = true;
      if (startLoop <= 2){ // Waits 40ms before returning true, as to allow the ball to pass through a bit. 
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

  /**
   * The current state of the sensor at the top point. Checks to see if its been pressed down so that it doesn't go any further past
   * @return true if not being pressed - dependant on the wiring of the limit switch for which is true.
   */
  public boolean intakeLimitTop() {
    return intakeLimitTop.get();
  }

  /**
   * The current state of the sensor at the bottom point. Checks to see if its been pressed down so that it doesn't go any further past
   * @return true if not being pressed - dependant on the wiring of the limit switch for which is true.
   */
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
    SmartDashboard.putNumber("Intake Sensor", lasersharkIntake.getDistanceInches());
  }
}
