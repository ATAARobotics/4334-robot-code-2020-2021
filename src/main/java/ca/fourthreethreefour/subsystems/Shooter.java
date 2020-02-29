/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.sensors.CANCoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import ca.fourthreethreefour.settings.Settings;
import ca.fourthreethreefour.vision.LimeLight;
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
  private CANCoder hoodEncoder = null;
  private LimeLight limeLight = null;
  private WPI_VictorSPX shooterHood = null;

  public Shooter(LimeLight limeLight) {
    flywheel1 = new CANSparkMax(Settings.FLYWHEEL_1_PORT, MotorType.kBrushless);
    flywheel2 = new CANSparkMax(Settings.FLYWHEEL_2_PORT, MotorType.kBrushless);
    shooterEncoder = new CANCoder(Settings.FLYWHEEL_ENCODER_PORT);
    hoodEncoder = new CANCoder(Settings.HOOD_ENCODER_PORT);
    this.limeLight = limeLight;
    shooterHood = new WPI_VictorSPX(Settings.SHOOTER_HOOD_PORT);

    flywheel1.setInverted(false);
    flywheel2.setInverted(false);

  }
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  public void flywheelSet(double speed) {
    flywheel1.set(speed);
    flywheel2.set(speed);
  }
  public double getRPM() {
    double TPM = shooterEncoder.getVelocity() * 600;//TPM = ticks per minute
    double RPM = TPM / Settings.TICKS_PER_FLYWHEEL_ROTATION;

    return RPM;
  }
  
  public void shooterHoodSet(double speed) {
    shooterHood.set(speed);
  }

  public double getEncoder() {
    return hoodEncoder.getAbsolutePosition();
  }

  public double getDistanceFromTarget() {
    return limeLight.getDistanceFromTarget();
  }
  
  public double getAngleToTarget() {
    return limeLight.getAngleToTarget();
  }

  public double getAngleToShoot() {
    return 0;
  }
}
