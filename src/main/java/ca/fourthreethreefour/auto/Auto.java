package ca.fourthreethreefour.auto;

import java.io.File;

import ca.fourthreethreefour.subsystems.Drive;
import ca.fourthreethreefour.subsystems.pid.DrivePID;
import ca.fourthreethreefour.subsystems.pid.TurnPID;
import edu.wpi.first.wpilibj.DriverStation;

public class Auto {
    private AutoFile autoFile = null;
    private Drive driveSubsystems = null;
    private DrivePID drivePID = null;
    private TurnPID turnPID = null;

    public Auto(Drive driveSubsystems) {
        this.driveSubsystems = driveSubsystems;
    }

    public void autoInit() {
        drivePID = new DrivePID(driveSubsystems);
        turnPID = new TurnPID(driveSubsystems);
        try {
            autoFile = new AutoFile(new File("/auto.txt"), driveSubsystems, drivePID, turnPID);
        } catch (Exception e) {
            DriverStation.reportWarning("no auto file detected!", true);
        }
        autoFile.init();
        
    }

    public void autoPeriodic() {
        autoFile.run();
    }

    public void autoDisabled() {
        autoFile.end();
    }

}