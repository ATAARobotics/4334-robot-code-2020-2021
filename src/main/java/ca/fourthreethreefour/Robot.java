/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour;

import ca.fourthreethreefour.auto.Auto;
import ca.fourthreethreefour.settings.Settings;
import ca.fourthreethreefour.subsystems.Cartridge;
import ca.fourthreethreefour.subsystems.Climb;
import ca.fourthreethreefour.subsystems.Drive;
import ca.fourthreethreefour.subsystems.Intake;
import ca.fourthreethreefour.subsystems.Shooter;
import ca.fourthreethreefour.subsystems.pid.AlignPID;
import ca.fourthreethreefour.subsystems.pid.DrivePID;
import ca.fourthreethreefour.subsystems.pid.FlywheelPID;
import ca.fourthreethreefour.subsystems.pid.TurnPID;
import ca.fourthreethreefour.teleop.Teleop;
import ca.fourthreethreefour.vision.LimeLight;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.TimedRobot;

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
  private LimeLight limeLight = new LimeLight();
  private DrivePID drivePID = null;
  private TurnPID turnPID = null;
  private FlywheelPID flywheelPID = null;
  private AlignPID alignPID = null;
  private Teleop teleop = null; 
  private Auto auto = null;
  

  private PowerDistributionPanel pdp = new PowerDistributionPanel(1);

  /**%
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() { 
    drivePID = new DrivePID(driveSubsystem);
    turnPID = new TurnPID(driveSubsystem);
    flywheelPID = new FlywheelPID(shooterSubsystem);
    alignPID = new AlignPID(limeLight);
    teleop = new Teleop(driveSubsystem, cartridgeSubsystem, rollerSubsystem, shooterSubsystem, climbSubsystem, flywheelPID);
    auto = new Auto(driveSubsystem, shooterSubsystem, cartridgeSubsystem, rollerSubsystem, drivePID, turnPID,
        flywheelPID, alignPID);
  }

  @Override
  public void disabledPeriodic() {
    settings.settingsPeriodic();
    // System.out.println(cartridgeSubsystem.indexerSensor());
  }

  @Override
  public void autonomousInit() {
    auto.autoInit();
  }

  @Override
  public void autonomousPeriodic() {
    auto.autoPeriodic();
  }

  @Override
  public void teleopInit() {
    auto.autoDisabled();
    teleop.teleopInit();
  }

  @Override
  public void teleopPeriodic() {
    teleop.teleopPeriodic();
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

  @Override
  public void disabledInit() {
    auto.autoDisabled();
  }

}
