package ca.fourthreethreefour.settings;

import java.io.File;

import edu.wpi.first.wpilibj.Timer;

public class Settings {

    static SettingsFile settingsFile = new SettingsFile(new File("/settings.txt"));
    String settingsActive = settingsFile.toString();

    static public int CONTROLLER_DRIVER_PORT = settingsFile.getIntProperty("CONTROLLER_DRIVER_PORT", 0);
    static public int CONTROLLER_OPERATOR_PORT = settingsFile.getIntProperty("CONTROLLER_OPERATOR_PORT", 1);

    static public int LEFT_FRONT_MOTOR_PORT = settingsFile.getIntProperty("LEFT_FRONT_MOTOR_PORT", 1);
    static public int LEFT_BACK_MOTOR_PORT = settingsFile.getIntProperty("LEFT_BACK_MOTOR_PORT", 2);
    static public int RIGHT_FRONT_MOTOR_PORT = settingsFile.getIntProperty("RIGHT_FRONT_MOTOR_PORT", 3);
    static public int RIGHT_BACK_MOTOR_PORT = settingsFile.getIntProperty("RIGHT_BACK_MOTOR_PORT", 4);

    static public int BELT_PORT = settingsFile.getIntProperty("BELT_PORT", 2);
    static public int INDEXER_PORT = settingsFile.getIntProperty("INDEXER_PORT", 0);
    static public int ROLLER_PORT = settingsFile.getIntProperty("ROLLER_PORT", 3);

    static public int FLYWHEEL_1_PORT = settingsFile.getIntProperty("FLYWHEEL_1_PORT", 4);
    static public int FLYWHEEL_2_PORT = settingsFile.getIntProperty("FLYWHEEL_2_PORT", 5);
    static public int FLYWHEEL_ENCODER_PORT = settingsFile.getIntProperty("FLYWHEEL_ENCODER_PORT", 22); 
    static public double FLYWHEEL_RPM_SETPOINT = settingsFile.getIntProperty("FLYWHEEL_RPM_SETPOINT", 2000);

    static public int ULTRASONIC_START_INPUT_PORT = settingsFile.getIntProperty("ULTRASONIC_START_INPUT_PORT", 0);
    static public int ULTRASONIC_START_OUTPUT_PORT = settingsFile.getIntProperty("ULTRASONIC_START_OUTPUT_PORT", 1);
    static public int ULTRASONIC_END_INPUT_PORT = settingsFile.getIntProperty("ULTRASONIC_END_INPUT_PORT", 2);
    static public int ULTARSONIC_END_OUTPUT_PORT = settingsFile.getIntProperty("ULTRASONIC_END_OUTPUT_PORT", 3);

    static public int CLIMB_RELEASE_1_PORT = settingsFile.getIntProperty("CLIMB_RELEASE_1_PORT", 7);
    static public int CLIMB_RELEASE_2_PORT = settingsFile.getIntProperty("CLIMB_RELEASE_2_PORT", 8);
    static public int CLIMB_GONDOLA_OF_DEATH_PORT = settingsFile.getIntProperty("CLIMB_GONDOLA_PORT", 6);

    static public boolean LOGGING_ENABLED = settingsFile.getBooleanProperty("LOGGING_ENABLED", false);

    static public double DRIVE_SPEED = settingsFile.getDoubleProperty("DRIVE_SPEED", 1);
    static public double TURN_SPEED = settingsFile.getDoubleProperty("TURN_SPEED", 1);

    static public double CARTRIDGE_INNER_SPEED = settingsFile.getDoubleProperty("CARTRIDGE_INNER_SPEED", 0.6);
    static public double CARTRIDGE_OUTER_SPEED = settingsFile.getDoubleProperty("CARTRIDGE_OUTER_SPEED", 0.6);
    static public double INDEXER_SPEED = settingsFile.getDoubleProperty("INDEXER_SPEED", 0.6);

    static public double ROLLER_SPEED = settingsFile.getDoubleProperty("ROLLER_SPEED", 0.8);

    static public double GONDOLA_SPEED = settingsFile.getDoubleProperty("GONDOLA_SPEED", 1);

    static public double FLYWHEEL_SPEED = settingsFile.getDoubleProperty("FLYWHEEL_SPEED", 0.6);
    static public int RPM_REFRESH_TIME = settingsFile.getIntProperty("RPM_REFRESH_TIME", 50);
    static public int TICKS_PER_FLYWHEEL_ROTATION = settingsFile.getIntProperty("TICKS_PER_FLYWHEEL_ROTATION", 1);

    public void settingsValueUpdate() {
        LOGGING_ENABLED = settingsFile.getBooleanProperty("LOGGING_ENABLED", false);
        DRIVE_SPEED = settingsFile.getDoubleProperty("DRIVE_SPEED", 1);
        TURN_SPEED = settingsFile.getDoubleProperty("TURN_SPEED", 1);

        CARTRIDGE_INNER_SPEED = settingsFile.getDoubleProperty("CARTRIDGE_INNER_SPEED", 0.6);
        CARTRIDGE_OUTER_SPEED = settingsFile.getDoubleProperty("CARTRIDGE_OUTER_SPEED", 0.6);
        INDEXER_SPEED = settingsFile.getDoubleProperty("INDEXER_SPEED", 0.6);

        ROLLER_SPEED = settingsFile.getDoubleProperty("ROLLER_SPEED", 0.8);

        GONDOLA_SPEED = settingsFile.getDoubleProperty("GONDOLA_SPEED", 1);

        FLYWHEEL_SPEED = settingsFile.getDoubleProperty("FLYWHEEL_SPEED", 0.6);
        RPM_REFRESH_TIME = settingsFile.getIntProperty("RPM_REFRESH_TIME", 50);
        TICKS_PER_FLYWHEEL_ROTATION = settingsFile.getIntProperty("TICKS_PER_FLYWHEEL_ROTATION", 1);
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