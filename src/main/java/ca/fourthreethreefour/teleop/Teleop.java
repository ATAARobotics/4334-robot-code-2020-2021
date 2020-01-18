package ca.fourthreethreefour.teleop;

import ca.fourthreethreefour.settings.Settings;
import ca.fourthreethreefour.subsystems.Drive;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;

public class Teleop {
    private XboxController controllerDriver = new XboxController(Settings.CONTROLLER_DRIVER_PORT);
    private Drive driveSubsystem = null;

    public Teleop(Drive driveSubsystem) {
        this.driveSubsystem = driveSubsystem;
    }
    public void teleopInit() {
        driveSubsystem.teleopInit();

    }

    private double previousSpeed = 0;
    private double previousTurn = 0;
    
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
    }
}