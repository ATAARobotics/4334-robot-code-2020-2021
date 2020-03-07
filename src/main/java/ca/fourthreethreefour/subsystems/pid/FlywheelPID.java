/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour.subsystems.pid;

import com.revrobotics.CANSparkMax;

import ca.fourthreethreefour.logging.Logging;
import ca.fourthreethreefour.settings.Settings;
import ca.fourthreethreefour.subsystems.Shooter;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj2.command.PIDSubsystem;
import edu.wpi.first.wpiutil.math.MathUtil;

public class FlywheelPID extends PIDSubsystem {
  /**
   * Creates a new FlywheelPID.
   */
  private Shooter shooterSubsystem = null;
  private SimpleMotorFeedforward feedforward = null;
  private double speed;


  public FlywheelPID(Shooter shooterSubsystem) {
    super(new PIDController(0.054, 0, 0.006)); //0.0045, 0, 0.0005
    
    this.shooterSubsystem = shooterSubsystem;
    getController().setTolerance(200);
    feedforward = new SimpleMotorFeedforward(0.00, 0.00144); //0.00, 0.00012
  }

  @Override
  public void useOutput(double output, double setpoint) {
    Logging.put("Output", output);
    output = MathUtil.clamp(output, 0, 12);
    // Use the output here
    // speed = MathUtil.clamp(output, 0, 1);
    
    // speed = MathUtil.clamp(output / 5, 0, 1) + 0.78;
    Logging.put("Feedforward", feedforward.calculate(setpoint));
    speed = feedforward.calculate(setpoint) + output; 

  }

  @Override
  public double getMeasurement() {
    // Return the process variable measurement here
    return shooterSubsystem.getRPM();
  }
  public double getSpeed() {

    return speed;

  }
}
