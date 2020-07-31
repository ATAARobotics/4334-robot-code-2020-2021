package ca.fourthreethreefour.teleop;

import ca.fourthreethreefour.logging.Logging;
import ca.fourthreethreefour.settings.Settings;
import ca.fourthreethreefour.subsystems.*;
import ca.fourthreethreefour.subsystems.pid.AlignPID;
import ca.fourthreethreefour.subsystems.pid.FlywheelPID;
import ca.fourthreethreefour.subsystems.pid.HoodPID;
import ca.fourthreethreefour.vision.LimeLight;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.XboxController;
/**
 * The class that controls all teleop functions
 */
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

    /**
     * Initializes the default states for all teleop functions
     */
    public void teleopInit() {
        limeLight.ledOff();
        driveSubsystem.teleopInit();
        driveSubsystem.reset();
        flywheelPID.setSetpoint(Settings.FLYWHEEL_SPEED_LINE);
        flywheelPID.enable();

    }

    // Variables for the code
    private double previousSpeed = 0;
    private double previousTurn = 0;
    private double hoodSpeed = 0;
    boolean cartridgeRun = false;   
    boolean highGear = false;
    boolean disableIntakeSensor = true;
    boolean stallIntake = false;
    int indexerFeed = 0;
    int cartridgeTime = 0;
    int shootFeed = 0;
    boolean shootAutoFeed = true;
    boolean shootIndexRun = false;
    int indexRunTime = 0;
    
    /**
     * The Periodic function for the robot. All robot controls are ran through here.
     * 
     * @Controls
     * 
     * Driver:
     *  Left Stick
     *  - X axis:
     *  - Y axis: Movement
     *  - Button: Virtual High Gear
     *  Right Stick
     *  - X axis: Turning
     *  - Y axis:
     *  - Button: Instant stop
     *  Left Trigger: Intake Wheels Out
     *  Right Trigger: Intake Wheels In
     *  Left Bumper: Decrease Flywheel Speed - 250 RPM
     *  Right Bumper: Increase Flywheel Speed - 250 RPM
     *  A:
     *  B:
     *  X: Limelight Align
     *  Y:
     *  Start: 
     *      Together with Operator: Release Climber - 50% Speed
     *  Back:
     *      Together with Operator: Release Climber - Full Speed
     *  D-Pad:
     *  - Up: Tower Setpoint
     *  - Right: Line Setpoint
     *  - Down: Close Trench Setpoint
     *  - Left: Far Trench Setpoint
     * 
     * Operator:
     *  Left Stick
     *  - X axis:
     *  - Y axis: Adjust Hood Position
     *  - Button:
     *  Right Stick
     *  - X axis:
     *  - Y axis: Adjust Intake Height
     *  - Button:
     *  Left Trigger:
     *  Right Trigger:
     *  Left Bumper: Belt Manual Forwards
     *  Right Bumper: Belt Manual Backwards
     *  A: Indexer Manual Backwards
     *  B: Indexer Manual Forwards
     *  X: Flywheel Manual Shooting
     *  Y: Automated Shooting
     *  Start: Toggle Auto Loading
     *      Together with Driver: Release Climber - 50% Speed
     *  Back: Cancel Cartridge Run
     *      Together with Driver: Release Climber - Full Speed
     *  D-Pad:
     *  - Up: Increase Hood PID - 2 Degrees
     *  - Right: Gondola Forwards
     *  - Down: Decrease Hood PID - 2 Degrees
     *  - Left: Gondola Backwards
     */
    public void teleopPeriodic() {

        //System.out.println(driveSubsystem.getNavX());
        // driveSubsystem.printEverything();
        Logging.put("Intake Toggle", disableIntakeSensor);
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
                    alignPID.getController().setTolerance(5);
                    alignPID.setSetpoint(0);
                    alignPID.enable();
                }
                if(alignPID.isEnabled() && alignPID.getController().atSetpoint() && limeLight.getTx() != 0){
                    turn = 0;
                    //controllerDriver.setRumble(GenericHID.RumbleType.kRightRumble, 0.5);
                    //controllerDriver.setRumble(GenericHID.RumbleType.kLeftRumble, 0.5);
                } else {
                    turn = alignPID.getRotateSpeed();
                }
            } else {
                if (alignPID.isEnabled()) {
                    limeLight.ledOff();
                    alignPID.disable();
                    controllerDriver.setRumble(GenericHID.RumbleType.kRightRumble, 0);
                    controllerDriver.setRumble(GenericHID.RumbleType.kLeftRumble, 0);
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
            shootAutoFeed = false;
        } else {
            shootAutoFeed = true;
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

        if (cartridgeRun) {
            if (cartridgeTime < 20) {
                cartridgeTime++;
            } else {
                cartridgeRun = false;
            }
        } else {
            cartridgeTime = 0;
        }
        
        if (controllerDriver.getTriggerAxis(Hand.kRight) > 0.1) {
            intakeSubsystem.intakeSet(controllerDriver.getTriggerAxis(Hand.kRight));
        } else if (controllerDriver.getTriggerAxis(Hand.kLeft) > 0.1) {
            intakeSubsystem.intakeSet(-controllerDriver.getTriggerAxis(Hand.kLeft));
        } else {
            intakeSubsystem.intakeSet(0);
        }

        SmartDashboard.putNumber("RPM", shooterSubsystem.getRPM());
        Logging.put("RPM_VALUE", shooterSubsystem.getRPM());

        if (controllerOperator.getXButton()) {
            shooterSubsystem.flywheelSet(1 * Settings.FLYWHEEL_SPEED);
        } else if (controllerOperator.getYButton()) {
            // if (!flywheelPID.isEnabled()) {
            //     flywheelPID.enable();
            // }
            shooterSubsystem.flywheelSet(flywheelPID.getSpeed());
            if (shootAutoFeed) {
                if (flywheelPID.getController().atSetpoint() || shootIndexRun) {
                    shootIndexRun = true;
                    cartridgeSubsystem.indexerSet(1);
                    // if (!cartridgeSubsystem.indexerSensor()){
                        // cartridgeSubsystem.beltSet(1);
                        // shootFeed = 0;
                    // } else if (shootFeed < 3){
                        // shootFeed++;
                        cartridgeSubsystem.beltSet(0.7);
                    // }
                } else {
                    if (!cartridgeSubsystem.indexerSensor()) {
                        cartridgeSubsystem.beltSet(1);
                        cartridgeSubsystem.indexerSet(-1);
                        indexerFeed = 0;
                    } else {
                        cartridgeSubsystem.beltSet(0);
                        if( indexerFeed < 2) {
                            cartridgeSubsystem.indexerSet(0.75);
                            indexerFeed++;
                        } else {
                            cartridgeSubsystem.indexerSet(0);
                        }
                    }
                }
            }
        } else {
            // if (flywheelPID.isEnabled()) {
            //     flywheelPID.disable();
            // }
            shooterSubsystem.flywheelSet(0);
        }

        if (shootIndexRun) {
            if (indexRunTime > 5) {
                shootIndexRun = false;
                indexRunTime = 0;
            } else {
                indexRunTime++;
            }
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
        } else if (controllerDriver.getStartButton() && controllerOperator.getStartButton()) {
            climbSubsystem.releaseSet(0.5);
        } else {
            climbSubsystem.releaseSet(0);
        }

        
        // TODO: Make sure this changes
        // if (controllerOperator.getStartButtonPressed()) {
            //  cartridgeRun = true;
        // }
        if (controllerOperator.getBackButtonPressed()) {
             cartridgeRun = false;
        }
        // if (controllerOperator.getStartButtonPressed()) {
        //     disableIntakeSensor = !disableIntakeSensor;
        // }
         
        if (!(disableIntakeSensor || controllerOperator.getBumper(Hand.kRight))) {       
            if (intakeSubsystem.intakeSensor()) {
                cartridgeRun = true;
            }
            SmartDashboard.putBoolean("Intake Disabled", false);
        } else {
            if(!disableIntakeSensor) { // if they want to see when its disable when bumper overides the auto, delete this
                SmartDashboard.putBoolean("Intake Disabled", false);
            } else {
                SmartDashboard.putBoolean("Intake Disabled", true);
            }
                cartridgeRun = false;
        }
      
        if (Math.abs(controllerOperator.getY(Hand.kLeft)) > 0.05) {
            if (hoodPID.isEnabled()) {
                hoodPID.disable();
            }
            if (controllerOperator.getY(Hand.kLeft) < 0) {
                hoodSpeed = controllerOperator.getY(Hand.kLeft) * Settings.HOOD_SPEED_UP;
            } else {
                hoodSpeed = controllerOperator.getY(Hand.kLeft) * Settings.HOOD_SPEED_DOWN;
            }
            
        } else if (!hoodPID.isEnabled()) {
            hoodSpeed = 0;
        }

        SmartDashboard.putNumber("Flywheel Setpoint", flywheelPID.getController().getSetpoint());
        Logging.put("Hood Setpoint", hoodPID.getController().getSetpoint());
        Logging.put("Hood Value", shooterSubsystem.getEncoder());
        Logging.put("Potentiometer", shooterSubsystem.getPotentiometer());

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
            // hoodPID.enable();
        } else if (controllerDriver.getPOV() == 270) {
            hoodPID.setSetpoint(Settings.HOOD_PID_FAR_TRENCH);
            flywheelPID.setSetpoint(Settings.FLYWHEEL_SPEED_FAR_TRENCH);
            // hoodPID.enable();
        } else if (controllerOperator.getPOV() == 0) {
            hoodPID.setSetpoint(hoodPID.getController().getSetpoint() + 2 < 55 ? hoodPID.getController().getSetpoint() + 2 : hoodPID.getController().getSetpoint());
            // hoodPID.enable();
        } else if (controllerOperator.getPOV() == 180) {
            hoodPID.setSetpoint(hoodPID.getController().getSetpoint() - 2 > 1.9 ? hoodPID.getController().getSetpoint() - 2 : hoodPID.getController().getSetpoint());
            // hoodPID.enable();
        } else if (controllerDriver.getAButtonPressed()) {
            // hoodPID.setSetpoint(shooterSubsystem.getAngleToShoot()); //TODO: add automated distance calculations 
            // hoodPID.enable(); 
            
        // } else if (hoodPID.isEnabled() && hoodPID.isDone()) {
        //     hoodPID.disable();
        // }
        } else if (controllerDriver.getBumperPressed(Hand.kLeft)) {
            flywheelPID.setSetpoint((flywheelPID.getController().getSetpoint() - 250 > 249 ? flywheelPID.getController().getSetpoint() - 250 : flywheelPID.getController().getSetpoint()));
        } else if (controllerDriver.getBumperPressed(Hand.kRight)) {
            flywheelPID.setSetpoint((flywheelPID.getController().getSetpoint() + 250 < 6726 ? flywheelPID.getController().getSetpoint() + 250 : flywheelPID.getController().getSetpoint()));
        }

        if (hoodPID.isEnabled()) {
            hoodSpeed = hoodPID.getSpeed();
        }
        
        shooterSubsystem.shooterHoodSet(hoodSpeed);
        
        double intakeSpeed = controllerOperator.getY(Hand.kRight);

        if (Math.abs(controllerOperator.getY(Hand.kRight)) < 0.15) {
            intakeSpeed = 0;
        } else {
            // stallIntake = false;
        }

        if (intakeSpeed < 0) {
            if (!intakeSubsystem.intakeLimitTop()) {
                intakeSpeed = 0;
            }
        } else if (intakeSpeed > 0) {
            if (!intakeSubsystem.intakeLimitBottom()) {
                intakeSpeed = 0;
                // stallIntake = true;
            }
        }

        // if(stallIntake){
        //     intakeSubsystem.runNeo(0.02);
        //     intakeSubsystem.stopVictor();
        // } else {
            intakeSubsystem.releaseSet(intakeSpeed);
        // }
        
    }
}