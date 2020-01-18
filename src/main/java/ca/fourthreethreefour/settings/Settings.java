package ca.fourthreethreefour.settings;

import java.io.File;

import edu.wpi.first.wpilibj.Timer;

public class Settings {

    static SettingsFile settingsFile = new SettingsFile(new File("/settings.txt"));
    String settingsActive = settingsFile.toString();

    static public int LEFT_FRONT_MOTOR_PORT = settingsFile.getIntProperty("LEFT_FRONT_MOTOR_PORT", 1);
    static public int LEFT_BACK_MOTOR_PORT = settingsFile.getIntProperty("LEFT_BACK_MOTOR_PORT", 2);
    static public int RIGHT_FRONT_MOTOR_PORT = settingsFile.getIntProperty("RIGHT_FRONT_MOTOR_PORT", 3);
    static public int RIGHT_BACK_MOTOR_PORT = settingsFile.getIntProperty("RIGHT_BACK_MOTOR_PORT", 4);

    static public int CONTROLLER_DRIVER_PORT = settingsFile.getIntProperty("CONTROLLER_DRIVER_PORT", 0);
    static public int CONTROLLER_OPERATOR_PORT = settingsFile.getIntProperty("CONTROLLER_OPERATOR_PORT", 1);
    
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