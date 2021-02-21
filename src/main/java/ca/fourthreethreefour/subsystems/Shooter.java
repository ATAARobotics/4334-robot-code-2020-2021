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
import edu.wpi.first.wpilibj.AnalogPotentiometer;
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
  private AnalogPotentiometer potentiometerEncoder = null;

  public Shooter(LimeLight limeLight) {
    flywheel1 = new CANSparkMax(Settings.FLYWHEEL_1_PORT, MotorType.kBrushless);
    flywheel2 = new CANSparkMax(Settings.FLYWHEEL_2_PORT, MotorType.kBrushless);
    shooterEncoder = new CANCoder(Settings.FLYWHEEL_ENCODER_PORT);
    hoodEncoder = new CANCoder(Settings.HOOD_ENCODER_PORT);
    this.limeLight = limeLight;
    shooterHood = new WPI_VictorSPX(Settings.SHOOTER_HOOD_PORT);
    potentiometerEncoder = new AnalogPotentiometer(Settings.POTENTIOMETER_ENCODER_PORT, Settings.HOOD_ENCODER_SCALE, Settings.HOOD_ENCODER_OFFSET);

    
    // So that + is one direction and - is another direction, we invert the motors so that its always consistent through all code usage.
    flywheel1.setInverted(false);
    flywheel2.setInverted(false);

    shooterHood.setInverted(true);

  }
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  /**
   * Sets the speed of the flywheel motors
   * @param speed - the value it should run at.
   */
  public void flywheelSet(double speed) {
    flywheel1.set(speed);
    flywheel2.set(speed);
  }

  /**
   * Sets the voltage of the flywheel motors
   * @param voltage - the voltage it should run at.
   */
  public void flywheelVoltageSet(double voltage) {
    flywheel1.setVoltage(voltage);
    flywheel2.setVoltage(voltage);
  }
  
  /**
   * Gets the RPM of the flywheel. Takes the velocity of the encoder, multiplies it so that its in ticks per minute, then divides it by the constant to get the RPM.
   * @return current RPM
   */
  public double getRPM() {
    double TPM = shooterEncoder.getVelocity() * 600;//TPM = ticks per minute
    double RPM = TPM / Settings.TICKS_PER_FLYWHEEL_ROTATION;

    return RPM;
  }

  /**
   * Gets the potentiometer value.
   * @return The current position of the potentiometer
   */
  public double getPotentiometer() {
    return potentiometerEncoder.get();
  }
  
  /**
   * Sets the speed of the hood motor. Should be a low value as its not an active motor.
   * @param speed - the value it should run at.
   */
  public void shooterHoodSet(double speed) {
    shooterHood.set(speed);
  }

  /**
   * Gets the current absolute position of the hood encoder. The internal settings for this is adjusted to return the direct angle.
   * @return The angle of the hood.
   */
  public double getEncoder() {
    return hoodEncoder.getAbsolutePosition();
  }

  /**
   * Gets the distance from the target as given by the limelight.
   * @return Distance between LimeLight and Target
   */
  public double getDistanceFromTarget() {
    return limeLight.getDistanceFromTarget();
  }
  
  /**
   * Gets the angle to the target as given by the limelight.
   * @return Angle between LimeLight and Target
   */
  public double getAngleToTarget() {
    return limeLight.getAngleToTarget();
  }

  public double getAngleToShoot() {
    return 0;
  }
}
