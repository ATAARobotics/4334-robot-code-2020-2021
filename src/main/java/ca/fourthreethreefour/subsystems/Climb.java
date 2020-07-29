/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import ca.fourthreethreefour.settings.Settings;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.Subsystem;

/**
 * Add your docs here.
 */
public class Climb implements Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  private WPI_TalonSRX releaseMotor1 = null;
  private WPI_VictorSPX releaseMotor2 = null;
  private WPI_TalonSRX gondolaMotor = null;
  private DigitalInput climbLimit = null;

  public Climb() {
    releaseMotor1 = new WPI_TalonSRX(Settings.CLIMB_RELEASE_1_PORT);
    releaseMotor2 = new WPI_VictorSPX(Settings.CLIMB_RELEASE_2_PORT);
    gondolaMotor = new WPI_TalonSRX(Settings.CLIMB_GONDOLA_OF_DEATH_PORT);
    climbLimit = new DigitalInput(Settings.CLIMB_LIMIT_PORT);
    
    // So that + is one direction and - is another direction, we invert the motors so that its always consistent through all code usage.
    releaseMotor1.setInverted(false);
    releaseMotor2.setInverted(false);
  }

  /**
   * Sets the speed of the release motors.
   * @param speed - the value it should run at. Is multiplied by the setting.
   */
  public void releaseSet(final double speed) {
    releaseMotor1.set(speed * Settings.CLIMB_SPEED);
    releaseMotor2.set(speed *Settings.CLIMB_SPEED);
  }

  /**
   * Sets the speed of the gondola
   * @param speed - the value it should run at. Is multiplied by the setting.
   */
  public void gondolaSet(final double speed) {
    gondolaMotor.set(speed * Settings.GONDOLA_SPEED);

  }

  int i = 0;
  boolean hasSeen = false;
  public boolean climbLimit() {
    if (climbLimit.get()){
      if (!hasSeen){
        i++;
        hasSeen = true;
      }
    } else {
      hasSeen = false;
    } 
    if (i > 0){
      return true;
    } else {
      return false;
    }
  }
  public void limitReset() {
    i = 0; 
  }
}
