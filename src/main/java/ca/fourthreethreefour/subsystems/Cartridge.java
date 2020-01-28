/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import ca.fourthreethreefour.settings.Settings;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Add your docs here.
 */
public class Cartridge extends Subsystem {
  private WPI_TalonSRX belt = null;
  private WPI_TalonSRX indexer = null;
  private Ultrasonic ultrasonicStart = null;
  private Ultrasonic ultrasonicEnd = null;
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  public Cartridge() {
    belt = new WPI_TalonSRX(Settings.BELT_PORT);
    indexer = new WPI_TalonSRX(Settings.INDEXER_PORT);
    ultrasonicStart = new Ultrasonic(Settings.ULTRASONIC_START_OUTPUT_PORT, Settings.ULTRASONIC_START_INPUT_PORT);
    ultrasonicEnd = new Ultrasonic(Settings.ULTARSONIC_END_OUTPUT_PORT, Settings.ULTRASONIC_END_INPUT_PORT);
    ultrasonicStart.setAutomaticMode(true);
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
  public void beltSet(double speed) {
    belt.set(speed);
  }
  public void indexerSet(double speed) {
    indexer.set(speed);
  }

  public boolean indexerSensor() {
    return false;
  }

  public void printUltrasonics() {
    System.out.println(ultrasonicStart.getRangeInches());
  }
  
  int startLoop = 0;
  boolean startBoolean = false;
  boolean hasSeen = false;
  public boolean cartridgeStart() {
    // if (startLoop >= 75) {
      if (startBoolean) {
        if (!(ultrasonicStart.getRangeInches() <= 7 || ultrasonicStart.getRangeInches() > 100 || hasSeen)) {
          startBoolean = false;
        }
        return false;
      } else if (ultrasonicStart.getRangeInches() <= 7 || ultrasonicStart.getRangeInches() > 100 || hasSeen) {
        if (startLoop < 30) {
          hasSeen = true;
          startLoop++;
          return false;
        } else {
          hasSeen = false;
          startBoolean = true;
          startLoop = 0;
          return true;
        } 
      } else {
        startLoop = 0;
        return false;
      }
    // } else {
    //   startLoop++;
    //   return false;
    // }
  }

  public boolean cartridgeEnd() {
    if (ultrasonicEnd.getRangeInches() <= 7 || ultrasonicEnd.getRangeInches() > 100) {
      return true;
    } else {
      return false;
    }
  
  }

  // public void resetLoops() {
  //   startLoop = 0;
  // }
}
