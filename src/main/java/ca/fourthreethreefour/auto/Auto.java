package ca.fourthreethreefour.auto;

import java.io.File;

import edu.wpi.first.wpilibj.DriverStation;

public class Auto {
   private AutoFile autoFile = null;

    public Auto() {

    }

    public void autoInit() {
        try {
            autoFile = new AutoFile(new File("/auto.txt"));
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