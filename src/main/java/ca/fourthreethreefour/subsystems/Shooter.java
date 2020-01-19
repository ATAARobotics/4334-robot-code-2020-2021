/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import ca.fourthreethreefour.settings.Settings;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Add your docs here.
 */
public class Shooter extends Subsystem {
  private WPI_TalonSRX flywheel = null;
  private long lastTime;
  private double lastTicks = 0;
  private double currentRPM;
  private Counter shooterEncoder = new Counter(Settings.FLYWHEEL_COUNTER_PORT);

  public Shooter() {
    flywheel = new WPI_TalonSRX(Settings.FLYWHEEL_PORT);
  }
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
  public void flywheelSet(double speed) {
    flywheel.set(speed);
  }

  public double getRPM() {

    double changeTime = (System.currentTimeMillis() - lastTime);

    if (changeTime < 50) {
      return currentRPM;
    }

    lastTime = System.currentTimeMillis();

    double currentTicks = shooterEncoder.get();
    double rate = (currentTicks - lastTicks)/ changeTime;

    System.out.println(rate);

    if (rate <= 0) {
      return 0;
    }
    
    lastTicks = currentTicks;

    double preCalculatedRPM = rate * 60 * 1000;
    currentRPM = currentRPM * 0.7 + preCalculatedRPM * 0.3;
    return currentRPM;
  }
}
