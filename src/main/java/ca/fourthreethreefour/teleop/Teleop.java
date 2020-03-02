package ca.fourthreethreefour.teleop;

import ca.fourthreethreefour.logging.Logging;
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
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class Teleop {
    private XboxController controllerDriver = new XboxController(Settings.CONTROLLER_DRIVER_PORT);
    private XboxController controllerOperator = new XboxController(Settings.CONTROLLER_OPERATOR_PORT);
    private Drive driveSubsystem = null;
    private Cartridge cartridgeSubsystem = null;
    private Intake intakeSubsystem = null;
    private Shooter shooterSubsystem = null;
    private Climb climbSubsystem = null; 

    
    private LimeLight limeLight = null;

    private FlywheelPID flywheelPID = null;
    private AlignPID alignPID = null;
    private HoodPID hoodPID = null;

    public Teleop(Drive driveSubsystem, Cartridge cartridgeSubsystem, Intake intakeSubsystem, Shooter shooterSubsystem, Climb climbSubsystem, LimeLight limeLight, FlywheelPID flywheelPID, AlignPID alignPID, HoodPID hoodPID) {
        this.driveSubsystem = driveSubsystem;
        this.cartridgeSubsystem = cartridgeSubsystem;
        this.intakeSubsystem = intakeSubsystem;
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
        flywheelPID.setSetpoint(Settings.FLYWHEEL_SPEED_LINE);

    }

    private double previousSpeed = 0;
    private double previousTurn = 0;
    private double hoodSpeed = 0;
    boolean cartridgeRun = false;   
    boolean highGear = false;
    boolean disableIntakeSensor = false;
    boolean trigger = false;
    int indexerFeed = 0;
    
    public void teleopPeriodic() {

        //System.out.println(driveSubsystem.getNavX());
        driveSubsystem.printEverything();
        Logging.put("Intake Toggle", disableIntakeSensor);
        Logging.put("Hood Position", shooterSubsystem.getEncoder());
        Logging.put("Intake Limit", intakeSubsystem.intakeLimitBottom());
    
        double speed;
        double turn;
        if (controllerDriver.getStickButton(Hand.kRight)) {
            speed = 0;
            turn = 0;
        } else {
            speed = controllerDriver.getY(Hand.kLeft) * 0.2 + previousSpeed * 0.8;
            previousSpeed = speed;
            speed = Math.copySign(speed * speed, speed);
            if (Math.abs(controllerDriver.getX(Hand.kRight)) >= 0.1) {
                if (alignPID.isEnabled()) {
                    limeLight.ledOff();
                    alignPID.disable();
                }
                turn = -controllerDriver.getX(Hand.kRight) * 0.1 + previousTurn * 0.9;
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

        // System.out.println(driveSubsystem.getVelocity());
        // System.out.println(driveSubsystem.getLeftEncoder());
        // System.out.println(driveSubsystem.getRightEncoder());
        
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

        if (controllerOperator.getBumper(Hand.kRight) || controllerOperator.getBumper(Hand.kLeft) || controllerOperator.getAButton() || controllerOperator.getBButton()) {
            if (controllerOperator.getBumper(Hand.kRight)) {
                cartridgeSubsystem.beltSet(-1);
            } else if (controllerOperator.getBumper(Hand.kLeft)) {
                cartridgeSubsystem.beltSet(1);
            }
            if (controllerOperator.getBButton() == true) {
                cartridgeSubsystem.indexerSet(1);
            } else if (controllerOperator.getAButton() == true) { 
                cartridgeSubsystem.indexerSet(-1);
            } 
        } else {
            if (cartridgeRun) {
                if (cartridgeSubsystem.indexerSensor()) {
                    if( indexerFeed < 5) {
                        cartridgeSubsystem.indexerSet(0.75);
                        indexerFeed++;
                    } else {
                        cartridgeSubsystem.indexerSet(0);
                    }
                } else {
                    indexerFeed = 0;
                    cartridgeSubsystem.indexerSet(-1);
                }
                if (cartridgeSubsystem.cartridgeEnd()) {
                    if (cartridgeSubsystem.indexerSensor()) {
                        cartridgeRun = false;
                    } else {
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
            } else {  
                cartridgeSubsystem.beltSet(0);
                cartridgeSubsystem.indexerSet(0);
                
            }
        }
        
        if (controllerDriver.getTriggerAxis(Hand.kRight) > 0.1) {
            intakeSubsystem.intakeSet(controllerDriver.getTriggerAxis(Hand.kRight));
        } else if (controllerDriver.getTriggerAxis(Hand.kLeft) > 0.1) {
            intakeSubsystem.intakeSet(-controllerDriver.getTriggerAxis(Hand.kLeft));
        } else {
            intakeSubsystem.intakeSet(0);
        }

        Logging.put("RPM", shooterSubsystem.getRPM());
        Logging.put("RPM_VALUE", shooterSubsystem.getRPM());

        if (controllerDriver.getBButton()) {
            shooterSubsystem.flywheelSet(1 * Settings.FLYWHEEL_SPEED);
        } else if (controllerDriver.getYButton()) {
            if (!flywheelPID.isEnabled()) {
                flywheelPID.enable();
            }
            shooterSubsystem.flywheelSet(flywheelPID.getSpeed());
            if (flywheelPID.getController().atSetpoint()) {
                cartridgeSubsystem.indexerSet(1);
                if (!cartridgeSubsystem.indexerSensor()){
                    cartridgeSubsystem.beltSet(1);
                }
            } else {
                if (!cartridgeSubsystem.indexerSensor()) {
                    cartridgeSubsystem.beltSet(1);
                    cartridgeSubsystem.indexerSet(-1);
                } else {
                    cartridgeSubsystem.beltSet(0);
                    cartridgeSubsystem.indexerSet(0);
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

        if (controllerDriver.getBackButton() && controllerOperator.getBackButton()) {
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
        if (controllerOperator.getStickButtonPressed(Hand.kRight)) {
            disableIntakeSensor = !disableIntakeSensor;
        }
         
        if (!(disableIntakeSensor || controllerOperator.getBumper(Hand.kRight))) {       
            if (intakeSubsystem.intakeSensor()) {
                cartridgeRun = true;
            }
            Logging.put("Intake Disabled", false);
        } else {
            if(!disableIntakeSensor) { // if they want to see when its disable when bumper overides the auto, delete this
                Logging.put("Intake Disabled", false);
            }else {
            Logging.put("Intake Disabled", true);
            }
                cartridgeRun = false;
        }
      
        if (Math.abs(controllerOperator.getY(Hand.kLeft)) > 0.05) {
            if (hoodPID.isEnabled()) {
                hoodPID.disable();
            }
            hoodSpeed = -controllerOperator.getY(Hand.kLeft) * Settings.HOOD_SPEED;
        } else if (!hoodPID.isEnabled()) {
            hoodSpeed = 0;
        }

        Logging.put("Flywheel Setpoint", flywheelPID.getController().getSetpoint());

        if (controllerDriver.getPOV() == 0) {
            hoodPID.setSetpoint(Settings.HOOD_PID_TOWER);
            flywheelPID.setSetpoint(Settings.FLYWHEEL_SPEED_TOWER);
            // hoodPID.enable();
        } else if (controllerDriver.getPOV() == 90) {
            hoodPID.setSetpoint(Settings.HOOD_PID_LINE);
            flywheelPID.setSetpoint(Settings.FLYWHEEL_SPEED_LINE);
            // hoodPID.enable();
        } else if (controllerDriver.getPOV() == 180) {
            hoodPID.setSetpoint(Settings.HOOD_PID_CLOSE_TRENCH);
            flywheelPID.setSetpoint(Settings.FLYWHEEL_SPEED_CLOSE_TRENCH);
            //hoodPID.enable();
        } else if (controllerDriver.getPOV() == 270) {
            hoodPID.setSetpoint(Settings.HOOD_PID_FAR_TRENCH);
            flywheelPID.setSetpoint(Settings.FLYWHEEL_SPEED_FAR_TRENCH);
            // hoodPID.enable();
        } else if (controllerDriver.getAButtonPressed()) {
            // hoodPID.setSetpoint(shooterSubsystem.getAngleToShoot()); //TODO: add automated distance calculations 
            // hoodPID.enable(); 
            
        } else if (hoodPID.isEnabled() && hoodPID.isDone()) {
            hoodPID.disable();
        }

        if (hoodPID.isEnabled()) {
            hoodSpeed = hoodPID.getSpeed();
        }
        
        shooterSubsystem.shooterHoodSet(hoodSpeed);
        // if(Math.abs(controllerOperator.getY(Hand.kRight)) > 0.05 ){
        //     intakeSubsystem.releaseSet(-controllerOperator.getY(Hand.kRight));
        //     trigger = false;
        // } else if (intakeSubsystem.intakeLimitTop()) {
        //     trigger = true;

        // } 
        // if(trigger){
        //     intakeSubsystem.runNeo(0.05);
        //     intakeSubsystem.stopVictor();
        // }
        
        if (controllerOperator.getY(Hand.kRight) < 0.05 && intakeSubsystem.intakeLimitTop()) {
            intakeSubsystem.releaseSet(controllerOperator.getY(Hand.kRight));
        } else if (controllerOperator.getY(Hand.kRight) > 0.05 && intakeSubsystem.intakeLimitBottom()){
            intakeSubsystem.releaseSet(controllerOperator.getY(Hand.kRight));
        } else {
            intakeSubsystem.releaseSet(0);
        }
        
       

        
    }
}