/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import ca.fourthreethreefour.auto.Auto;
import ca.fourthreethreefour.logging.Logging;
import ca.fourthreethreefour.settings.Settings;
import ca.fourthreethreefour.subsystems.Cartridge;
import ca.fourthreethreefour.subsystems.Climb;
import ca.fourthreethreefour.subsystems.Drive;
import ca.fourthreethreefour.subsystems.Intake;
import ca.fourthreethreefour.subsystems.Shooter;
import ca.fourthreethreefour.subsystems.pid.AlignPID;
import ca.fourthreethreefour.subsystems.pid.DrivePID;
import ca.fourthreethreefour.subsystems.pid.FlywheelPID;
import ca.fourthreethreefour.subsystems.pid.HoodPID;
import ca.fourthreethreefour.subsystems.pid.TurnPID;
import ca.fourthreethreefour.teleop.Teleop;
import ca.fourthreethreefour.vision.LimeLight;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

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
  private Intake intakeSubsystem = new Intake();
  private LimeLight limeLight = new LimeLight();
  private Shooter shooterSubsystem = new Shooter(limeLight);
  private Climb climbSubsystem = new Climb();
  private DrivePID drivePID = null;
  private TurnPID turnPID = null;
  private FlywheelPID flywheelPID = null;
  private AlignPID alignPID = null;
  private HoodPID hoodPID = null;
  private Teleop teleop = null; 
  private Auto auto = null;
  private Logging logging = null;
  

  private PowerDistributionPanel pdp = new PowerDistributionPanel(0);

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
    hoodPID = new HoodPID(shooterSubsystem);
    teleop = new Teleop(driveSubsystem, cartridgeSubsystem, intakeSubsystem, shooterSubsystem, climbSubsystem, 
        limeLight, flywheelPID, alignPID, hoodPID);
    auto = new Auto(driveSubsystem, shooterSubsystem, cartridgeSubsystem, intakeSubsystem, drivePID, turnPID,
        flywheelPID, alignPID, hoodPID);
    logging = new Logging(shooterSubsystem, flywheelPID, cartridgeSubsystem);
  }

  @Override
  public void robotPeriodic() {
    Logging.put("Left Encoder", driveSubsystem.getLeftEncoder());
    Logging.put("Right Encoder", driveSubsystem.getRightEncoder());
    Logging.put("Encoder Total", driveSubsystem.getEncoder());
    Logging.put("NavX Angle", driveSubsystem.getNavX());
    SmartDashboard.putNumber("Hood Angle", shooterSubsystem.getEncoder());
    intakeSubsystem.printUltrasonics();
    cartridgeSubsystem.printUltrasonics();
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
    CommandScheduler.getInstance().run();
    logging.record();
  }

  @Override
  public void teleopInit() {
    auto.autoDisabled();
    teleop.teleopInit();
  }

  @Override
  public void teleopPeriodic() {
    teleop.teleopPeriodic();
    CommandScheduler.getInstance().run();
    logging.record();
  }
  double angle;

  @Override
  public void testInit() {
    angle = shooterSubsystem.getEncoder();
  }

  double speed = 0;
  @Override
  public void testPeriodic() {
    if (angle - shooterSubsystem.getEncoder() > 0.5) {
      shooterSubsystem.shooterHoodSet(speed);
      speed -= 0.001;
    }
    Logging.put("Hood static", speed);
    Logging.log(""+speed);


    
  }

  @Override
  public void disabledInit() {
    auto.autoDisabled();
    logging.write();
  }

}
