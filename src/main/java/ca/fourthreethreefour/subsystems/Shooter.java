/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.sensors.CANCoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import ca.fourthreethreefour.settings.Settings;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj2.command.Subsystem;

/**
 * Add your docs here.
 */
public class Shooter implements Subsystem {
  private CANSparkMax flywheel1 = null;
  private CANSparkMax flywheel2 = null;
  private long lastTime;
  private double lastTicks = 0;
  private double currentRPM;
 // private Counter shooterEncoder = new Counter(Settings.FLYWHEEL_COUNTER_PORT);
  private CANCoder shooterEncoder = null;

  public Shooter() {
    flywheel1 = new CANSparkMax(Settings.FLYWHEEL_1_PORT, MotorType.kBrushless);
    flywheel2 = new CANSparkMax(Settings.FLYWHEEL_2_PORT, MotorType.kBrushless);
    shooterEncoder = new CANCoder(Settings.FLYWHEEL_ENCODER_PORT);

  }
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  public void flywheelSet(double speed) {
    flywheel1.set(speed * Settings.FLYWHEEL_SPEED);
    flywheel2.set(speed * Settings.FLYWHEEL_SPEED);
  }
  public double getRPM() {
    double TPM = shooterEncoder.getVelocity() * 60;//TPM = ticks per minute
    double RPM = Settings.TICKS_PER_FLYWHEEL_ROTATION / TPM;

    return RPM;
  }

  /**
   * Manual calculation code. Based on a sensor that activates magnetically.
   */
  // public double getRPMManual() {

  //   double changeTime = (System.currentTimeMillis() - lastTime);

  //   if (changeTime < Settings.RPM_REFRESH_TIME) {
  //     return currentRPM;
  //   }

  //   lastTime = System.currentTimeMillis();

  //   double currentTicks = shooterEncoder.get();
  //   double rate = (currentTicks - lastTicks)/ changeTime;

  //   System.out.println(rate);

  //   if (rate <= 0) {
  //     return 0;
  //   }
    
  //   lastTicks = currentTicks;

  //   double preCalculatedRPM = (rate / Settings.TICKS_PER_FLYWHEEL_ROTATION) * 60 * 1000;
  //   currentRPM = currentRPM * 0.7 + preCalculatedRPM * 0.3;
  //   return currentRPM;
  // }
}
