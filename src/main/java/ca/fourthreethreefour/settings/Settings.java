package ca.fourthreethreefour.settings;

import java.io.File;

import edu.wpi.first.wpilibj.Timer;

public class Settings {

    static SettingsFile settingsFile = new SettingsFile(new File("/settings.txt"));
    String settingsActive = settingsFile.toString();
    
    static public boolean LOGGING_ENABLED = settingsFile.getBooleanProperty("LOGGING_ENABLED", false);

    public void settingsValueUpdate() {
        LOGGING_ENABLED = settingsFile.getBooleanProperty("LOGGING_ENABLED", false);
    }

    public void settingsPeriodic() {
        try {
            settingsFile.reload();
        } catch (NullPointerException e) {
            Timer.delay(0.2);
        }

        if (!settingsActive.equalsIgnoreCase(settingsFile.toString())) {
            System.out.println("reloading settings");
            settingsValueUpdate();
            settingsActive = settingsFile.toString();
        }
    }
}