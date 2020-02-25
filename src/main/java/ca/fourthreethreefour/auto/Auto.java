package ca.fourthreethreefour.auto;

import java.io.File;

import ca.fourthreethreefour.subsystems.Cartridge;
import ca.fourthreethreefour.subsystems.Drive;
import ca.fourthreethreefour.subsystems.Intake;
import ca.fourthreethreefour.subsystems.Shooter;
import ca.fourthreethreefour.subsystems.pid.AlignPID;
import ca.fourthreethreefour.subsystems.pid.DrivePID;
import ca.fourthreethreefour.subsystems.pid.FlywheelPID;
import ca.fourthreethreefour.subsystems.pid.TurnPID;
import edu.wpi.first.wpilibj.DriverStation;

public class Auto {
    private AutoFile autoFile = null;

    public Auto(Drive driveSubsystems, Shooter shooterSubsystem, Cartridge cartridgeSubsystem, Intake intakeSubsystem, DrivePID drivePID, TurnPID turnPID, FlywheelPID flywheelPID, AlignPID alignPID) {
        autoFile = new AutoFile(driveSubsystems, shooterSubsystem, cartridgeSubsystem,
                intakeSubsystem, drivePID, turnPID, flywheelPID, alignPID);
    }

    public void autoInit() {
        try {
            autoFile.init(new File("/auto.txt"));
        } catch (Exception e) {
            DriverStation.reportWarning("no auto file detected!", false);
        }
    }

    public void autoPeriodic() {
        autoFile.run();
    }

    public void autoDisabled() {
        autoFile.end();
    }

}