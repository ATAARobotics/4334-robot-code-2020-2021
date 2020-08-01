/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour.subsystems.pid;

import ca.fourthreethreefour.subsystems.Drive;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDSubsystem;
import edu.wpi.first.wpiutil.math.MathUtil;

public class DrivePID extends PIDSubsystem {
  private Drive driveSubsystem = null;

  /**
   * Creates a new DrivePID.
   */
  private double speed;
  public DrivePID(Drive driveSubsystem) {

    super(new PIDController(0.003, 0, 0.017)); // TODO: Update PID values
    this.driveSubsystem = driveSubsystem;
  }

  @Override
  public void useOutput(double output, double setpoint) {
    // Use the output here
    speed = MathUtil.clamp(output, -1, 1); // Clamps to ensure no value greater or less than constraints.
  }

  @Override
  public double getMeasurement() {
    // Return the process variable measurement here
    return driveSubsystem.getEncoder();
  }

  /**
   * 
   * @return Speed value
   */
  public double getSpeed() {
    return speed;
  }
  
}
