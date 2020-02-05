/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour.subsystems.pid;

import ca.fourthreethreefour.vision.LimeLight;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDSubsystem;

public class AlignPID extends PIDSubsystem {

  private LimeLight limeLight;
  private double rotateSpeed;

  public AlignPID() {
    super(
        // The PIDController used by the subsystem
        new PIDController(0.1, 0, 0));
  }

  @Override
  public void useOutput(double output, double setpoint) {
    rotateSpeed = output;
  }

  @Override
  public double getMeasurement() {
    // Return the process variable measurement here
    return limeLight.getTx();
  }

  public double getRotateSpeed() {
    return rotateSpeed;
  }

}
