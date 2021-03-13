/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour.subsystems.pid;

import ca.fourthreethreefour.logging.Logging;
import ca.fourthreethreefour.subsystems.Shooter;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj2.command.PIDSubsystem;
import edu.wpi.first.wpiutil.math.MathUtil;

public class HoodPID extends PIDSubsystem {
  private Shooter shooterSubsystem = null;
  private SimpleMotorFeedforward hoodFeedforward = null;

  private double speed;
  /**
   * Creates a new HoodPID.
   */
  public HoodPID(Shooter shooterSubsystem) {
    super(
        // The PIDController used by the subsystem
        new PIDController(-0.02, 0, 0));
    this.shooterSubsystem = shooterSubsystem;
    getController().setTolerance(0.5); // Tolerance is in Degrees
    hoodFeedforward = new SimpleMotorFeedforward(0.25, 0);
  }

  @Override
  public void useOutput(double output, double setpoint) {
    MathUtil.clamp(output, -0.6, 0.6); // Clamps to the absolute max and minimum of what we want it to output.
    Logging.put("Hood Output", output);
    speed = output + hoodFeedforward.calculate(setpoint); // Takes the feedforward, calculates the power, and adds it to output to get the final speed
    // Use the output here
  }

  @Override
  public double getMeasurement() {
    // Return the process variable measurement here
    return shooterSubsystem.getEncoder();
  }

  public double getSpeed() {
    return speed;
  }

  int i;
  /**
   * Checks multiple times if the controller is at the setpoint before stating that it is.
   * @return true if on setpoint for specified amount of time.
   */
  public boolean isDone() {
    if (i < 4000) {
      if (getController().atSetpoint()) {
        i++;
        return false;
      } else {
        i = 0;
        return false;
      }
    } else {
      return true;
    }
  }
}
