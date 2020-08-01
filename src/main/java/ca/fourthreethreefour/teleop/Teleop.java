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
        flywheelPID.enable(); // NOTE: We are able to leave this PID running constantly, as it DOES NOT directly control any mechanism. It only ever does calculations. Do not do this if it actually controls something.

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
    

        // The logic here is to set the variables speed and turn, and modify them based on the scenario
        // So that down below, you only have to call arcadeDrive once per loop. No risks of double calling.
        double speed;
        double turn;
        if (controllerDriver.getStickButton(Hand.kRight)) { // Instant stop
            speed = 0;
            turn = 0;
        } else {
            speed = controllerDriver.getY(Hand.kLeft) * 0.2 + previousSpeed * 0.8; // To achieve proper acceleration, rather than 0 to 100, it only does 20% of the current input and 80% of the previous input.
            previousSpeed = speed; // This means that it both accelerates steadily and deaccelerates steadily. Doesn't effect how drivers feel it controls, but helps prevent stalling heavily.
            speed = Math.copySign(speed * speed, speed); // Squares the speed, keeps the sign
            if (Math.abs(controllerDriver.getX(Hand.kRight)) >= 0.1) {
                if (alignPID.isEnabled()) { // If trying to turn, makes sure the limelight align is disabled.
                    limeLight.ledOff();
                    alignPID.disable();
                }
                turn = -controllerDriver.getX(Hand.kRight) * 0.1 + previousTurn * 0.9; // Similar logic to above, just different values.
                previousTurn = turn; // Note, this variable is initialized outside the function, so it remains to the next loop.
                turn = Math.copySign(turn * turn, turn);
            } else if (controllerDriver.getXButton()) {
                if (!alignPID.isEnabled()) { // To ensure it doesn't try to initalize the align values every single loop, it checks first if it needs to then does it.
                    limeLight.ledOn();
                    alignPID.getController().setTolerance(5);
                    alignPID.setSetpoint(0);
                    alignPID.enable();
                }
                if(alignPID.isEnabled() && alignPID.getController().atSetpoint() && limeLight.getTx() != 0){ // Ensures that it doesn't try giving float values when on point. Safety precaution.
                    turn = 0;
                    //controllerDriver.setRumble(GenericHID.RumbleType.kRightRumble, 0.5);
                    //controllerDriver.setRumble(GenericHID.RumbleType.kLeftRumble, 0.5);
                } else {
                    turn = alignPID.getRotateSpeed();
                }
            } else {
                if (alignPID.isEnabled()) { // Ensures it only attempts to disable it once, rather than every loop which takes up resources.
                    limeLight.ledOff();
                    alignPID.disable();
                    controllerDriver.setRumble(GenericHID.RumbleType.kRightRumble, 0);
                    controllerDriver.setRumble(GenericHID.RumbleType.kLeftRumble, 0);
                }
                turn = previousTurn * 0.9; // Deaccelates if not active turning nor is aligning. The speed does it regularly, but as this is part of a if else, needs this check at the very end.
                previousTurn = turn;
                turn = Math.copySign(turn * turn, turn);
            }
        }
        driveSubsystem.arcadeDrive(speed, turn, false); // We square all the values before now, so we don't want to square them a second time.

        // System.out.println(driveSubsystem.getVelocity());
        // System.out.println(driveSubsystem.getLeftEncoder());
        // System.out.println(driveSubsystem.getRightEncoder());
        
        // A virtual high gear. We didn't need to use a physical gear system for speed this season as the neos had enough power as is, but wanted some differencing between high and low
        // As such, we created a virtual gear system.

        
        if (Math.abs(controllerDriver.getY(Hand.kLeft)) < 0.05 || Math.abs(controllerDriver.getX(Hand.kRight)) > 0.05) {
            // Here we want it to set to low speed when turning, or when coming to a stop.
            driveSubsystem.speedLow();
            highGear = false;
        } else if (controllerDriver.getStickButton(Hand.kLeft) && Math.abs(driveSubsystem.getVelocity()) > 1950) {
            // An issue we faced this season was voltage drawing, mainly a spike that would occur when trying to go from a low speed to an extremely high speed.
            // It would pull more voltage than the PDP would allow, causing the breaker to reset, create something like it browning out.
            // So, we have it be that the robot must be going an already fast speed before they can activate the high gear, so that it wouldn't cause these issues.
            driveSubsystem.speedHigh();
            highGear = true;
        } else {
            driveSubsystem.speedLow();
            highGear = false; 
        }

        // The cartridge system. To allow for overriding of mechanisms, we check the buttons first before running anything automated.
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
            shootAutoFeed = false; // To override later during shooting, when the buttons are being pressed its set to false, all other times true
        } else {
            shootAutoFeed = true; // When the systems being overridden, it prevents the auto feeding from running. Otherwise it is allowed to run when needed.


            if (cartridgeRun) { // The system for automatically loading. This variable is set to true down below.

                /* The indexer part of the system. If its detected, runs it in for 5 loops, then stops.
                    Clears the count when no ball is there. Runs it backwards to prevent jamming while balls are incoming. */
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

                
                // The bulk of the cartridge system.
                if (cartridgeSubsystem.cartridgeEnd()) {
                    if (cartridgeSubsystem.indexerSensor()) { // If a ball is there at the cartridge end, and in the indexer, doesn't feed any further.
                        cartridgeRun = false;
                    } else {
                        // If there is no ball at the indexer sensor, it can continue to move. Using the same logic as the loop right below.
                        if (!cartridgeSubsystem.cartridgeStart()) {
                            cartridgeSubsystem.beltSet(1);
                        } else {
                            cartridgeSubsystem.beltSet(0);
                        }
                    }
                } else {
                    /*
                        The logic here is as follows. To allow for spacing out of balls, it feeds in a ball, and stops it at the cartridge start point
                        Then, when another ball comes in, it feeds along until that ball is at the start. This all continues until it cannot load any
                        more balls without it jamming.
                    */
                    if (!cartridgeSubsystem.cartridgeStart()) {
                        cartridgeSubsystem.beltSet(1);
                    } else {
                        cartridgeSubsystem.beltSet(0);
                        cartridgeRun = false;
                    }
                }
            } else {  
                // Makes sure it doesnt run when its not supposed to.
                cartridgeSubsystem.beltSet(0);
                cartridgeSubsystem.indexerSet(0);
                
            }
        }

        /*
            To prevent it from running too extremely long, when the main loop is running, it counts down 20 loops and then stops the auto feeding
            The 20 (400ms) loops allow for to stop right in the right place in the case of the sensors not detecting. It's not reliable enough
            to rely entirely on it, but it gives that level of security.
        */
        if (cartridgeRun) {
            if (cartridgeTime < 20) {
                cartridgeTime++;
            } else {
                cartridgeRun = false;
            }
        } else {
            cartridgeTime = 0;
        }
        
        // The Intake system.
        if (controllerDriver.getTriggerAxis(Hand.kRight) > 0.1) {
            intakeSubsystem.intakeSet(controllerDriver.getTriggerAxis(Hand.kRight));
        } else if (controllerDriver.getTriggerAxis(Hand.kLeft) > 0.1) {
            // Since the two triggers give a value from [0.0,+1.0], one value must be inversed so it can reverse the intakes.
            intakeSubsystem.intakeSet(-controllerDriver.getTriggerAxis(Hand.kLeft));
        } else {
            intakeSubsystem.intakeSet(0);
        }

        SmartDashboard.putNumber("RPM", shooterSubsystem.getRPM());
        Logging.put("RPM_VALUE", shooterSubsystem.getRPM());

        // Shooting system.
        if (controllerOperator.getXButton()) { // Manual override.
            shooterSubsystem.flywheelVoltageSet(11 * Settings.FLYWHEEL_SPEED);
        } else if (controllerOperator.getYButton()) {
            // if (!flywheelPID.isEnabled()) {
            //     flywheelPID.enable();
            // }
            shooterSubsystem.flywheelVoltageSet(flywheelPID.getSpeed()); // The flywheelPID is always running, and only ever returns a speed value (never actually controls a mechanism directly). Here we use that value.
            if (shootAutoFeed) { // The variable from above comes into use here. Ensures this only runs when it isn't being overridden.
                if (flywheelPID.getController().atSetpoint() || shootIndexRun) {
                    /*
                        When it's at the setpoint, it runs the indexer to feed the ball into the flywheel.
                        It also runs the belt, as the logic behind it is that if its able to fire,
                        there is no risk of also continuing to feed balls. It wouldn't risk jamming.
                    */
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
                    // If its not ready to fire, it premptively gets the next ball into position for immediate firing.
                    if (!cartridgeSubsystem.indexerSensor()) {
                        // No ball in the indexer? Runs the belt towards it, but the indexer runs opposite so that it prevents going in too far immediately.
                        cartridgeSubsystem.beltSet(1);
                        cartridgeSubsystem.indexerSet(-1);
                        indexerFeed = 0;
                    } else {
                        // When the ball is detected by the indexer sensor, it stops the belt, and feeds the ball in just enough and then stops.
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
            shooterSubsystem.flywheelVoltageSet(0);
        }

        /*
            Similar to the cartridgeRun logic above, we want the shooting to last for a bit (to account for fluctuations)
            so it is set when the flywheel is on setpoint, and runs for a bit, but this loop sets it back to false after 100ms.
            Its enough time so that it runs for just long enough to be able to fire.
            This logic might be able to be removed and that would allow for additional consistency, but thats only if the
            flywheel's speed is more consistent itself.
        */
        if (shootIndexRun) {
            if (indexRunTime > 5) {
                shootIndexRun = false;
                indexRunTime = 0;
            } else {
                indexRunTime++;
            }
        }

        // Gondola code. Unfortunately never was used, but would work when a gondola is installed.
        if (controllerOperator.getPOV( ) == 90) {
            climbSubsystem.gondolaSet(1);
        } else if (controllerOperator.getPOV() == 270) {
            climbSubsystem.gondolaSet(-1);
        } else {
            climbSubsystem.gondolaSet(0);
        }

        // Climb code. Designed to require both driver and operator hold down the button, and for two speed options (as we needed additional precision, but also speed)
        if (controllerDriver.getBackButton() && controllerOperator.getBackButton()) {
            climbSubsystem.releaseSet(1);
        } else if (controllerDriver.getStartButton() && controllerOperator.getStartButton()) {
            climbSubsystem.releaseSet(0.5);
        } else {
            climbSubsystem.releaseSet(0);
        }

        // if (controllerOperator.getStartButtonPressed()) {
            //  cartridgeRun = true;
        // }
        if (controllerOperator.getBackButtonPressed()) {
             cartridgeRun = false;
        }
        // Able to toggle the intake sensor system on and off.
        // if (controllerOperator.getStartButtonPressed()) {
        //     disableIntakeSensor = !disableIntakeSensor;
        // }
        
        if (!(disableIntakeSensor || controllerOperator.getBumper(Hand.kRight))) { 
            // If the intake sensor detects the ball, runs the automatic intaking system.      
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
        
        // Hood controls

        // Similar to the drive speed, we set a hood speed variable so that we don't risk calling the hood function more than once.
        if (Math.abs(controllerOperator.getY(Hand.kLeft)) > 0.05) {
            if (hoodPID.isEnabled()) {
                hoodPID.disable(); // Manual controls, we want to make sure the hoodPID was disabled.
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

        // Setpoints, for flywheel and hood.
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
        } else if (controllerOperator.getPOV() == 0) { // Hood PID control. To allow for more precision in movement, can adjust it by specific degrees rather than by feel.
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
        } else if (controllerDriver.getBumperPressed(Hand.kLeft)) { // Flywheel PID control. For more precision in shooting if needed, can manually adjust it.
            flywheelPID.setSetpoint((flywheelPID.getController().getSetpoint() - 250 > 249 ? flywheelPID.getController().getSetpoint() - 250 : flywheelPID.getController().getSetpoint()));
        } else if (controllerDriver.getBumperPressed(Hand.kRight)) {
            flywheelPID.setSetpoint((flywheelPID.getController().getSetpoint() + 250 < 6726 ? flywheelPID.getController().getSetpoint() + 250 : flywheelPID.getController().getSetpoint()));
        }

        if (hoodPID.isEnabled()) { // This is below the setpoint setting rather than with the other direct hoodSpeed stuff, so it adjusts the target point and THEN actually gets the value for them.
            hoodSpeed = hoodPID.getSpeed();
        }
        
        shooterSubsystem.shooterHoodSet(hoodSpeed);
        

        // Intaking raising and lowering
        double intakeSpeed = controllerOperator.getY(Hand.kRight); // Sets the intakeSpeed first, as we are then checking for when it SHOULDN"T be ran, aka when it should be set to 0.

        if (Math.abs(controllerOperator.getY(Hand.kRight)) < 0.15) {
            intakeSpeed = 0;
        } else {
            // stallIntake = false;
        }

        /*
            So that it doesn't go too far, there are limit switches on the top and bottom to detect when its at the limits.
            This switches can be either set to return true when pressed or true when released, depending on the physical wiring.
            We had it set to be true when released, as a safety precaution. In case it ever got unplugged, it would return false,
            so having false mean too far prevents accidental breakage if the limit switch isn't working.

            The logic below checks only one switch depending on the direction. Checking both at the same time is useless, and just
            makes things more confusing. So we only check the respective one and only have to deal with that.
        */
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