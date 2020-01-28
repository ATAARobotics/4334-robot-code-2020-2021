package ca.fourthreethreefour.auto;

import java.io.File;

import ca.fourthreethreefour.subsystems.Drive;
import edu.wpi.first.wpilibj.DriverStation;

public class Auto {
    private AutoFile autoFile = null;
    private Drive driveSubsystems = null;

    public Auto(Drive driveSubsystems) {
        this.driveSubsystems = driveSubsystems;
    }

    public void autoInit() {
        try {
            autoFile = new AutoFile(new File("/auto.txt"), driveSubsystems);
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