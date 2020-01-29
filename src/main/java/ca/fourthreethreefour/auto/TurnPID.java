/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour.auto;

import ca.fourthreethreefour.subsystems.Drive;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDSubsystem;
import edu.wpi.first.wpiutil.math.MathUtil;

public class TurnPID extends PIDSubsystem {
  private Drive driveSubsystem = null;
  private double turn;
  /**
   * Creates a new TurnPID.
   */
  public TurnPID(Drive driveSubsystem) {
    super(new PIDController(0, 0, 0)); //TODO: Update PID values
    this.driveSubsystem = driveSubsystem;
  }

  @Override
  public void useOutput(double output, double setpoint) {
    // Use the output here
    turn = MathUtil.clamp(output, -1, 1);
  }

  @Override
  public double getMeasurement() {
    // Return the process variable measurement here
    return driveSubsystem.getNavX();
  }

  public double getTurn() {
    // Return the turn value
    return turn;
  }
}
