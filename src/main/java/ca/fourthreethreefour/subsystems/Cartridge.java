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

import ca.fourthreethreefour.logging.Logging;
import ca.fourthreethreefour.settings.Settings;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Subsystem;

/**
 * Add your docs here.
 */
public class Cartridge implements Subsystem {
  private WPI_VictorSPX innerBelt = null;
  private WPI_TalonSRX outerBelt = null;
  private WPI_TalonSRX indexer = null;
  private Lasershark lasersharkStart = null;
  private Lasershark lasersharkEnd = null;
  private Lasershark lasersharkIndexer = null;
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  public Cartridge() {
    innerBelt = new WPI_VictorSPX(Settings.INNER_BELT_PORT);
    outerBelt = new WPI_TalonSRX(Settings.OUTER_BELT_PORT);
    indexer = new WPI_TalonSRX(Settings.INDEXER_PORT);
    lasersharkStart = new Lasershark(Settings.LINESHARK_START_PORT);
    lasersharkEnd = new Lasershark(Settings.LINESHARK_END_PORT);
    lasersharkIndexer = new Lasershark(Settings.LINESHARK_INDEXER_PORT);

    // So that + is one direction and - is another direction, we invert the motors so that its always consistent through all code usage.
    outerBelt.setInverted(false);
    innerBelt.setInverted(true);
    indexer.setInverted(false);
  }
  
  /**
   * Sets the speed of both the inner and outer belt
   * @param speed - the value it should run at. Is multiplied by the setting.
   */
  public void beltSet(double speed) {
    innerBelt.set(speed * Settings.CARTRIDGE_INNER_SPEED);
    outerBelt.set(speed * Settings.CARTRIDGE_OUTER_SPEED);
  }
  
  /**
   * Sets the speed of the indexer
   * @param speed - the value it should run at. Is multiplied by the setting.
   */
  public void indexerSet(double speed) {
    indexer.set(speed * Settings.INDEXER_SPEED);
  }

  /**
   * Checks to see if the indexer sensor detects a ball
   * @return true if sensor detects a ball.
   */
  public boolean indexerSensor() {
    if (indexerGet() <= 4) {
      return true;
    } else {
      return false;
    }
    
  }

  public void printUltrasonics() {
    SmartDashboard.putNumber("Start Sensor", lasersharkStart.getDistanceInches());
    SmartDashboard.putNumber("End Sensor", lasersharkEnd.getDistanceInches());
    SmartDashboard.putNumber("Indexer Sensor", lasersharkIndexer.getDistanceInches());
  }
  
  int startLoop = 0;
  boolean startBoolean = false;
  boolean hasSeen = false;
  /**
   * Checks to see if the cartridge start sensor detects a ball. Only will return true once, then not return true until the ball has left and a new one is detected.
   * Prone to not detect the next ball if two balls are too close. That is an issue.
   * @return true if sensor has detected a fresh ball.
   */
  public boolean cartridgeStart() {
      if (startBoolean) {
        if (!(startGet() <= 3 || hasSeen)) { // Checks to see if the ball is out of range, or if the seen flag is false.
          startBoolean = false; // If both are the case, unsets the boolean flag, as the ball has left the sensor and a new one can be detected
        }
        return false;
      } else if (startGet() <= 3 || hasSeen) { // Ball is closer than 3 inches or has previously been seen
        if (startLoop < 1) { // First loop, sets a flag that it has been seen, increments, still returns false.
          hasSeen = true;
          startLoop++;
          return false;
        } else { // Further loops, the flag is unset, the startBoolean is set to true, loop is reset, returns true.
          hasSeen = false;
          startBoolean = true;
          startLoop = 0;
          return true;
        }
      } else { // If nothing is being seen, clears the startLoop. As a precaution.
        startLoop = 0;
        return false;
      }
  }

  /**
   * Checks to see if the cartridge end sensor detects a ball
   * @return true if sensor detects a ball.
   */
  public boolean cartridgeEnd() {
    if (endGet() <= 3) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Returns the value of the indexer lasershark sensor.
   * @return Lasershark value (Inches)
   */
  public double indexerGet() {
    return lasersharkIndexer.getDistanceInches();
  }

  /**
   * Returns the value of the end lasershark sensor.
   * @return Lasershark value (Inches)
   */
  public double endGet() {
    return lasersharkEnd.getDistanceInches();
  }

  /**
   * Returns the value of the start lasershark sensor.
   * @return Lasershark value (Inches)
   */
  public double startGet() {
    return lasersharkStart.getDistanceInches();
  }
  // public void resetLoops() {
  //   startLoop = 0;
  // }
}
