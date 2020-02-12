package ca.fourthreethreefour.teleop;

import ca.fourthreethreefour.settings.Settings;
import ca.fourthreethreefour.subsystems.Cartridge;
import ca.fourthreethreefour.subsystems.Climb;
import ca.fourthreethreefour.subsystems.Drive;
import ca.fourthreethreefour.subsystems.Intake;
import ca.fourthreethreefour.subsystems.Shooter;
import ca.fourthreethreefour.subsystems.pid.AlignPID;
import ca.fourthreethreefour.subsystems.pid.FlywheelPID;
import ca.fourthreethreefour.subsystems.pid.HoodPID;
import ca.fourthreethreefour.vision.LimeLight;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;

public class Teleop {
    private XboxController controllerDriver = new XboxController(Settings.CONTROLLER_DRIVER_PORT);
    private XboxController controllerOperator = new XboxController(Settings.CONTROLLER_OPERATOR_PORT);
    private Drive driveSubsystem = null;
    private Cartridge cartridgeSubsystem = null;
    private Intake rollerSubsystem = null;
    private Shooter shooterSubsystem = null;
    private Climb climbSubsystem = null; 
    
    private LimeLight limeLight = null;

    private FlywheelPID flywheelPID = null;
    private AlignPID alignPID = null;
    private HoodPID hoodPID = null;

    public Teleop(Drive driveSubsystem, Cartridge cartridgeSubsystem, Intake rollerSubsystem, Shooter shooterSubsystem, Climb climbSubsystem, LimeLight limeLight, FlywheelPID flywheelPID, AlignPID alignPID, HoodPID hoodPID) {
        this.driveSubsystem = driveSubsystem;
        this.cartridgeSubsystem = cartridgeSubsystem;
        this.rollerSubsystem = rollerSubsystem;
        this.shooterSubsystem = shooterSubsystem;
        this.climbSubsystem = climbSubsystem;
        this.flywheelPID = flywheelPID;
        this.alignPID = alignPID;
        this.hoodPID = hoodPID;
        this.limeLight = limeLight;
    }
    public void teleopInit() {
        driveSubsystem.teleopInit();
        driveSubsystem.reset();
        flywheelPID.setSetpoint(Settings.FLYWHEEL_RPM_SETPOINT);

    }

    private double previousSpeed = 0;
    private double previousTurn = 0;
    private double hoodSpeed = 0;
    boolean cartridgeRun = false;   
    boolean highGear = false;
    
    public void teleopPeriodic() {
        double speed;
        double turn;
        if (controllerDriver.getStickButton(Hand.kRight)) {
            speed = 0;
            turn = 0;
        } else {
            if (highGear) {
                speed = controllerDriver.getY(Hand.kLeft) * 0.2 + previousSpeed * 0.8;
            } else {
                speed = controllerDriver.getY(Hand.kLeft) * 0.1 + previousSpeed * 0.9;
            }
            previousSpeed = speed;
            speed = Math.copySign(speed * speed, speed);
            if (Math.abs(controllerDriver.getX(Hand.kRight)) >= 0.1) {
                if (alignPID.isEnabled()) {
                    limeLight.ledOff();
                    alignPID.disable();
                }
                turn = controllerDriver.getX(Hand.kRight) * 0.1 + previousTurn * 0.9;
                previousTurn = turn;
                turn = Math.copySign(turn * turn, turn);
            } else if (controllerDriver.getXButton()) {
                if (!alignPID.isEnabled()) {
                    limeLight.ledOn();
                    alignPID.enable();
                    alignPID.getController().setTolerance(2);
                    alignPID.setSetpoint(0);
                }
                turn = alignPID.getRotateSpeed();
            } else {
                if (alignPID.isEnabled()) {
                    limeLight.ledOff();
                    alignPID.disable();
                }
                turn = previousTurn * 0.9;
                previousTurn = turn;
                turn = Math.copySign(turn * turn, turn);
            }
        }
        driveSubsystem.arcadeDrive(speed, turn, false);

        System.out.println(driveSubsystem.getVelocity());

        if (Math.abs(controllerDriver.getY(Hand.kLeft)) < 0.05 || Math.abs(controllerDriver.getX(Hand.kRight)) > 0.05) {
            driveSubsystem.speedLow();
            highGear = false;
        } else if (controllerDriver.getStickButton(Hand.kLeft) && Math.abs(driveSubsystem.getVelocity()) > 1950) {
            driveSubsystem.speedHigh();
            highGear = true;
        } else {
            driveSubsystem.speedLow();
            highGear = false; 
        }

        if (controllerOperator.getTriggerAxis(Hand.kRight) > 0.1 || controllerOperator.getTriggerAxis(Hand.kLeft) > 0.1 || controllerOperator.getAButton() || controllerOperator.getBButton()) {
            if (controllerOperator.getTriggerAxis(Hand.kRight) > 0.1) {
                double cartridgeSpeed = controllerOperator.getTriggerAxis(Hand.kRight);
                cartridgeSubsystem.beltSet(cartridgeSpeed);
            } else if (controllerOperator.getTriggerAxis(Hand.kLeft) > 0.1) {
                double cartridgeSpeed = -controllerOperator.getTriggerAxis(Hand.kLeft);
                cartridgeSubsystem.beltSet(cartridgeSpeed);
            } else {
                cartridgeSubsystem.beltSet(0);
            }
            if (controllerOperator.getAButton() == true) {
                cartridgeSubsystem.indexerSet(1);
            } else if (controllerOperator.getBButton() == true) { 
                cartridgeSubsystem.indexerSet(-1);
            } else {
                cartridgeSubsystem.indexerSet(0);
            }
        } else {
            if (cartridgeRun) {
                if (cartridgeSubsystem.cartridgeEnd()) {
                    if (cartridgeSubsystem.indexerSensor()) {
                        cartridgeSubsystem.indexerSet(0);
                        cartridgeRun = false;
                    } else {
                        cartridgeSubsystem.indexerSet(1);
                        if (!cartridgeSubsystem.cartridgeStart()) {
                            cartridgeSubsystem.beltSet(1);
                        } else {
                            cartridgeSubsystem.beltSet(0);
                        }
                    }
                } else {
                    if (!cartridgeSubsystem.cartridgeStart()) {
                        cartridgeSubsystem.beltSet(1);
                    } else {
                        cartridgeSubsystem.beltSet(0);
                        cartridgeRun = false;
                    }
                }
            }
        }
        
        if (controllerDriver.getTriggerAxis(Hand.kRight) > 0.1) {
            rollerSubsystem.intakeSet(controllerDriver.getTriggerAxis(Hand.kRight));
        } else if (controllerDriver.getTriggerAxis(Hand.kLeft) > 0.1) {
            rollerSubsystem.intakeSet(-controllerDriver.getTriggerAxis(Hand.kLeft));
        } else {
            rollerSubsystem.intakeSet(0);
        }

        if (controllerDriver.getYButton()) {
            if (!flywheelPID.isEnabled()) {
                flywheelPID.enable();
            }
            shooterSubsystem.flywheelSet(flywheelPID.getSpeed());

            if (flywheelPID.getController().atSetpoint()) {
                cartridgeSubsystem.indexerSet(1);
                if (!cartridgeSubsystem.indexerSensor()) {
                    cartridgeSubsystem.beltSet(1);
                }
            }
        } else {
            if (flywheelPID.isEnabled()) {
                flywheelPID.disable();
            }
            shooterSubsystem.flywheelSet(0);
        }

        if (controllerOperator.getPOV( ) == 90) {
            climbSubsystem.gondolaSet(1);
        } else if (controllerOperator.getPOV() == 270) {
            climbSubsystem.gondolaSet(-1);
        } else {
            climbSubsystem.gondolaSet(0);
        }

        if (controllerDriver.getStartButton() && controllerOperator.getStartButton()) {
            climbSubsystem.releaseSet(1);
        } else {
            climbSubsystem.releaseSet(0);
        }

        // TODO: Make sure this changes
         if (controllerOperator.getStartButtonPressed()) {
             cartridgeRun = true;
         }
         if (controllerOperator.getBackButtonPressed()) {
             cartridgeRun = false;
         }
      
        if (rollerSubsystem.intakeSensor()) {
            cartridgeRun = true;
        }
      
        if (controllerOperator.getBumper(Hand.kLeft)) {
            if (hoodPID.isEnabled()) {
                hoodPID.disable();
            }
            hoodSpeed = 1;
        } else if (controllerOperator.getBumper(Hand.kRight)) {
            if (hoodPID.isEnabled()) {
                hoodPID.disable();
            }
            hoodSpeed = -1;
        } else if (!hoodPID.isEnabled()) {
            hoodSpeed = 0;
        }

        if (controllerDriver.getPOV() == 0) {
            hoodPID.setSetpoint(Settings.HOOD_PID_UP);
            hoodPID.enable();
        } else if (controllerDriver.getPOV() == 90) {
            hoodPID.setSetpoint(Settings.HOOD_PID_RIGHT);
            hoodPID.enable();
        } else if (controllerDriver.getPOV() == 180) {
            hoodPID.setSetpoint(Settings.HOOD_PID_DOWN);
            hoodPID.enable();
        } else if (controllerDriver.getPOV() == 270) {
            hoodPID.setSetpoint(Settings.HOOD_PID_LEFT);
            hoodPID.enable();
        } else if (controllerDriver.getAButtonPressed()) {
            hoodPID.setSetpoint(0); //TODO: add automated distance calculations 
            hoodPID.enable(); 
        } else if (hoodPID.isEnabled() && hoodPID.getController().atSetpoint()) {
            hoodPID.disable();
        }

        if (hoodPID.isEnabled()) {
            hoodSpeed = hoodPID.getSpeed();
        }
        shooterSubsystem.shooterHoodSet(hoodSpeed);

        rollerSubsystem.releaseSet(controllerOperator.getY(Hand.kRight));
    }
}