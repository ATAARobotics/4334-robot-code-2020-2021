/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import ca.fourthreethreefour.settings.Settings;
import ca.fourthreethreefour.subsystems.Cartridge;
import ca.fourthreethreefour.subsystems.Climb;
import ca.fourthreethreefour.subsystems.Drive;
import ca.fourthreethreefour.subsystems.Intake;
import ca.fourthreethreefour.subsystems.Shooter;
import ca.fourthreethreefour.subsystems.pid.FlywheelPID;
import ca.fourthreethreefour.teleop.Teleop;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Settings settings = new Settings();
  private Drive driveSubsystem = new Drive();
  private Cartridge cartridgeSubsystem = new Cartridge();
  private Intake rollerSubsystem = new Intake();
  private Shooter shooterSubsystem = new Shooter();
  private Climb climbSubsystem = new Climb();
  private FlywheelPID flywheelPID = null;
  private Teleop teleop = null; 

  private PowerDistributionPanel pdp = new PowerDistributionPanel(1);

  private WPI_TalonSRX leftEncoder = new WPI_TalonSRX(30);
  private WPI_TalonSRX rightEncoder = new WPI_TalonSRX(31);

  /**%
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() { 
    flywheelPID = new FlywheelPID(shooterSubsystem);
    teleop = new Teleop(driveSubsystem, cartridgeSubsystem, rollerSubsystem, shooterSubsystem, climbSubsystem, flywheelPID);
  }
  @Override
  public void disabledPeriodic() {
    settings.settingsPeriodic();
    // System.out.println(cartridgeSubsystem.indexerSensor());
  }

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    teleop.teleopInit();
    leftEncoder.setSensorPhase(false);
    rightEncoder.setSensorPhase(false);

  }

  @Override
  public void teleopPeriodic() {
    teleop.teleopPeriodic();
    SmartDashboard.putNumber("Left Velocity", leftEncoder.getSelectedSensorVelocity());
    SmartDashboard.putNumber("Right Velocty", rightEncoder.getSelectedSensorVelocity());
    SmartDashboard.putNumber("Left Position", leftEncoder.getSelectedSensorPosition());
    SmartDashboard.putNumber("Right Position", rightEncoder.getSelectedSensorPosition());
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

}
