package ca.fourthreethreefour.teleop;

import ca.fourthreethreefour.settings.Settings;
import ca.fourthreethreefour.subsystems.Cartridge;
import ca.fourthreethreefour.subsystems.Climb;
import ca.fourthreethreefour.subsystems.Drive;
import ca.fourthreethreefour.subsystems.Intake;
import ca.fourthreethreefour.subsystems.Shooter;
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

    public Teleop(Drive driveSubsystem, Cartridge cartridgeSubsystem, Intake rollerSubsystem, Shooter shooterSubsystem, Climb climbSubsystem) {
        this.driveSubsystem = driveSubsystem;
        this.cartridgeSubsystem = cartridgeSubsystem;
        this.rollerSubsystem = rollerSubsystem;
        this.shooterSubsystem = shooterSubsystem;
        this.climbSubsystem = climbSubsystem;
    }
    public void teleopInit() {
        driveSubsystem.teleopInit();

    }

    private double previousSpeed = 0;
    private double previousTurn = 0;
    boolean temporary = false;   
    
    public void teleopPeriodic() {
        double speed;
        if (Math.abs(controllerDriver.getY(Hand.kLeft)) < 0.05) {
            speed = 0;
        } else {
            speed = controllerDriver.getY(Hand.kLeft) * 0.1 + previousSpeed * 0.9;
        }
        previousSpeed = speed;
        double turn;
        if (Math.abs(controllerDriver.getX(Hand.kRight)) < 0.05) {
            turn = 0;
        } else {
            turn = controllerDriver.getX(Hand.kRight) * 0.1 + previousTurn * 0.9;
        }
        previousTurn = turn;
        driveSubsystem.arcadeDrive(speed, turn, true);

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
        if (controllerDriver.getTriggerAxis(Hand.kRight) > 0.1) {
            rollerSubsystem.set(controllerDriver.getTriggerAxis(Hand.kRight));
        } else if (controllerDriver.getTriggerAxis(Hand.kLeft) > 0.1) {
            rollerSubsystem.set(-controllerDriver.getTriggerAxis(Hand.kLeft));
        } else {
            rollerSubsystem.set(0);
        }

        if (controllerOperator.getYButton()) {
            shooterSubsystem.flywheelSet(1);
        } else {
            shooterSubsystem.flywheelSet(0);
        }
      
         if (controllerOperator.getStartButtonPressed()) {
             temporary = true;
         }
         if (controllerOperator.getBackButtonPressed()) {
             temporary = false;
         }
      
        // if (rollerSubsystem.intakeSensor()) {
        if (temporary) {
            // if (controllerOperator.getStartButtonPressed()) {
            //     cartridgeSubsystem.resetLoops();
            // }
            if (cartridgeSubsystem.cartridgeEnd()) {
                if (cartridgeSubsystem.indexerSensor()) {
                    cartridgeSubsystem.indexerSet(0);
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
                    temporary = false;
                }
            }
        }
    }
}