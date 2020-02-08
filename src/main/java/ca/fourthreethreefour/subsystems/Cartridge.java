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
public class Cartridge implements Subsystem {
  private WPI_TalonSRX innerBelt = null;
  private WPI_TalonSRX outerBelt = null;
  private WPI_TalonSRX indexer = null;
  private Lasershark lasersharkStart = null;
  private Lasershark lasersharkEnd = null;
  private Lasershark lasersharkIndexer = null;
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  public Cartridge() {
    innerBelt = new WPI_TalonSRX(Settings.INNER_BELT_PORT);
    outerBelt = new WPI_TalonSRX(Settings.OUTER_BELT_PORT);
    indexer = new WPI_TalonSRX(Settings.INDEXER_PORT);
    lasersharkStart = new Lasershark(Settings.LINESHARK_START_PORT);
    lasersharkEnd = new Lasershark(Settings.LINESHARK_END_PORT);
    lasersharkIndexer = new Lasershark(Settings.LINESHARK_INDEXER_PORT);
  }
  
  public void beltSet(double speed) {
    innerBelt.set(speed);
    outerBelt.set(speed);
  }
  
  public void indexerSet(double speed) {
    indexer.set(speed * Settings.INDEXER_SPEED);
  }

  public boolean indexerSensor() {
    if (lasersharkIndexer.getDistanceInches() <= 4) {
      return true;
    } else {
      return false;
    }
    
  }

  public void printUltrasonics() {
    System.out.println(lasersharkStart.getDistanceInches());
  }
  
  int startLoop = 0;
  boolean startBoolean = false;
  boolean hasSeen = false;
  public boolean cartridgeStart() {
    // if (startLoop >= 75) {
      if (startBoolean) {
        if (!(lasersharkStart.getDistanceInches() <= 4 || hasSeen)) {
          startBoolean = false;
        }
        return false;
      } else if (lasersharkStart.getDistanceInches() <= 4 || hasSeen) {
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
    if (lasersharkEnd.getDistanceInches() <= 4) {
      return true;
    } else {
      return false;
    }
  
  }



  // public void resetLoops() {
  //   startLoop = 0;
  // }
}
