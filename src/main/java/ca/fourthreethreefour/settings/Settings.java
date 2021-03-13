package ca.fourthreethreefour.settings;

import edu.wpi.first.wpilibj.Timer;

import java.io.File;

public class Settings {

    static SettingsFile settingsFile = new SettingsFile(new File("/FILES/settings.txt"));
    String settingsActive = settingsFile.toString();

    static public int CONTROLLER_DRIVER_PORT = settingsFile.getIntProperty("CONTROLLER_DRIVER_PORT", 0);
    static public int CONTROLLER_OPERATOR_PORT = settingsFile.getIntProperty("CONTROLLER_OPERATOR_PORT", 1);

    static public int LEFT_FRONT_MOTOR_PORT = settingsFile.getIntProperty("LEFT_FRONT_MOTOR_PORT", 1);
    static public int LEFT_BACK_MOTOR_PORT = settingsFile.getIntProperty("LEFT_BACK_MOTOR_PORT", 2);
    static public int RIGHT_FRONT_MOTOR_PORT = settingsFile.getIntProperty("RIGHT_FRONT_MOTOR_PORT", 3);
    static public int RIGHT_BACK_MOTOR_PORT = settingsFile.getIntProperty("RIGHT_BACK_MOTOR_PORT", 4);

    static public int LEFT_ENCODER_PORT = settingsFile.getIntProperty("LEFT_ENCODER_PORT", 20);
    static public int RIGHT_ENCODER_PORT = settingsFile.getIntProperty("RIGHT_ENCODER_PORT", 21);

    static public int INNER_BELT_PORT = settingsFile.getIntProperty("INNER_BELT_PORT", 35);
    static public int OUTER_BELT_PORT = settingsFile.getIntProperty("OUTER_BELT_PORT", 31);
    static public int INDEXER_PORT = settingsFile.getIntProperty("INDEXER_PORT", 30);
  
    static public int INTAKE_PORT = settingsFile.getIntProperty("INTAKE_PORT", 33);
    static public int INTAKE_RELEASE_1_PORT = settingsFile.getIntProperty("INTAKE_RELEASE_1_PORT", 32);
    static public int INTAKE_RELEASE_2_PORT = settingsFile.getIntProperty("INTAKE_RELEASE_2_PORT", 7);
    static public int INTAKE_LIMIT_TOP_PORT = settingsFile.getIntProperty("INTAKE_LIMIT_TOP_PORT", 4);
    static public int INTAKE_LIMIT_BOTTOM_PORT = settingsFile.getIntProperty("INTAKE_LIMIT_BOTTOM_PORT", 5);

    static public int FLYWHEEL_1_PORT = settingsFile.getIntProperty("FLYWHEEL_1_PORT", 5);
    static public int FLYWHEEL_2_PORT = settingsFile.getIntProperty("FLYWHEEL_2_PORT", 6);
  
    static public int FLYWHEEL_ENCODER_PORT = settingsFile.getIntProperty("FLYWHEEL_ENCODER_PORT", 22); 
    static public int SHOOTER_HOOD_PORT = settingsFile.getIntProperty("SHOOTER_HOOD_PORT", 34);
    static public int HOOD_ENCODER_PORT = settingsFile.getIntProperty("HOOD_ENCODER_PORT", 23);
    static public int POTENTIOMETER_ENCODER_PORT = settingsFile.getIntProperty("POTENTIOMETER_ENCODER_PORT", 0);
    static public double HOOD_ENCODER_SCALE = settingsFile.getDoubleProperty("HOOD_ENCODER_SCALE", 110);
    static public double HOOD_ENCODER_OFFSET = settingsFile.getDoubleProperty("HOOD_ENCODER_OFFSET", -39.5);

    static public int LINESHARK_START_PORT = settingsFile.getIntProperty("LINESHARK_START_PORT", 2);
    static public int LINESHARK_END_PORT = settingsFile.getIntProperty("LINESHARK_END_PORT", 1);

    static public int LINESHARK_INDEXER_PORT = settingsFile.getIntProperty("LINESHARK_INDEXER_PORT", 0);
    static public int LINESHARK_INTAKE_PORT = settingsFile.getIntProperty("LINESHARK_INTAKE_PORT", 3);

    static public int CLIMB_RELEASE_1_PORT = settingsFile.getIntProperty("CLIMB_RELEASE_1_PORT", 36);
    static public int CLIMB_RELEASE_2_PORT = settingsFile.getIntProperty("CLIMB_RELEASE_2_PORT", 37);
    static public int CLIMB_GONDOLA_OF_DEATH_PORT = settingsFile.getIntProperty("CLIMB_GONDOLA_PORT", 9);
    static public int CLIMB_LIMIT_PORT =  settingsFile.getIntProperty("CLIMB_LIMIT_PORT", 9);

    static public boolean LOGGING_ENABLED = settingsFile.getBooleanProperty("LOGGING_ENABLED", false);

    static public double DRIVE_SPEED = settingsFile.getDoubleProperty("DRIVE_SPEED", 0.7);
    static public double TURN_SPEED = settingsFile.getDoubleProperty("TURN_SPEED", 0.9);
    static public double DRIVE_MAX_SPEED = settingsFile.getDoubleProperty("DRIVE_MAX_SPEED", 1);

    static public double CARTRIDGE_INNER_SPEED = settingsFile.getDoubleProperty("CARTRIDGE_INNER_SPEED", 0.5);
    static public double CARTRIDGE_OUTER_SPEED = settingsFile.getDoubleProperty("CARTRIDGE_OUTER_SPEED", 0.4);
    static public double INDEXER_SPEED = settingsFile.getDoubleProperty("INDEXER_SPEED", 0.4);

    static public double INTAKE_SPEED = settingsFile.getDoubleProperty("INTAKE_SPEED", 0.8);
    static public double RELEASE_SPEED = settingsFile.getDoubleProperty("RELEASE_SPEED", 0.5);
    static public double RELEASE_NEO_SPEED = settingsFile.getDoubleProperty("RELEASE_NEO_SPEED", 0.5); 

    static public double GONDOLA_SPEED = settingsFile.getDoubleProperty("GONDOLA_SPEED", 1);
    static public double CLIMB_SPEED = settingsFile.getDoubleProperty("CLIMB_SPEED", 0.5);

    static public double FLYWHEEL_SPEED = settingsFile.getDoubleProperty("FLYWHEEL_SPEED", 1);
    static public double HOOD_SPEED_DOWN = settingsFile.getDoubleProperty("HOOD_SPEED_DOWN", 0.5);
    static public double HOOD_SPEED_UP = settingsFile.getDoubleProperty("HOOD_SPEED_UP", 0.5);
    static public int TICKS_PER_FLYWHEEL_ROTATION = settingsFile.getIntProperty("TICKS_PER_FLYWHEEL_ROTATION", 4096);

    static public double HOOD_PID_ZONE4 = settingsFile.getDoubleProperty("HOOD_PID_ZONE4", 36);
    static public double HOOD_PID_ZONE2 = settingsFile.getDoubleProperty("HOOD_PID_ZONE2", 28);
    static public double HOOD_PID_ZONE1 = settingsFile.getDoubleProperty("HOOD_PID_ZONE1", 2);
    static public double HOOD_PID_ZONE3 = settingsFile.getDoubleProperty("HOOD_PID_ZONE3", 32);

    static public double FLYWHEEL_SPEED_ZONE4 = settingsFile.getDoubleProperty("FLYWHEEL_SPEED_ZONE4", 7000);
    static public double FLYWHEEL_SPEED_ZONE2 = settingsFile.getDoubleProperty("FLYWHEEL_SPEED_ZONE2", 6000);
    static public double FLYWHEEL_SPEED_ZONE1 = settingsFile.getDoubleProperty("FLYWHEEL_SPEED_ZONE1", 6000);
    static public double FLYWHEEL_SPEED_ZONE3 = settingsFile.getDoubleProperty("FLYWHEEL_SPEED_ZONE3", 6000);

    static public double LIMELIGHT_ANGLE_FROM_FLAT = settingsFile.getDoubleProperty("LIMELIGHT_ANGLE_FROM_FLAT", 0);
    static public double LIMELIGHT_DISTANCE_FROM_GROUND = settingsFile.getDoubleProperty("LIMELIGHT_DISTANCE_FROM_GROUND", 0);
    static public double TARGET_DEFAULT_HEIGHT = settingsFile.getDoubleProperty("TARGET_DEFAULT_HEIGHT", 0);

    static public String AUTO_ROUTINE = settingsFile.getStringProperty("AUTO_ROUTINE", "default");

    /**
     * Dynamic Settings. Values that want to be able to be changed on the fly (speeds, not ports) are grabbed here, with the formula running again
     * To make a variable dynamic, put the ZONE2 a second time down here. Make sure default values are the same.
     */
    public void settingsValueUpdate() {
        LOGGING_ENABLED = settingsFile.getBooleanProperty("LOGGING_ENABLED", false);
        DRIVE_SPEED = settingsFile.getDoubleProperty("DRIVE_SPEED", 0.7);
        TURN_SPEED = settingsFile.getDoubleProperty("TURN_SPEED", 0.9);

        CARTRIDGE_INNER_SPEED = settingsFile.getDoubleProperty("CARTRIDGE_INNER_SPEED", 0.5);
        CARTRIDGE_OUTER_SPEED = settingsFile.getDoubleProperty("CARTRIDGE_OUTER_SPEED", 0.4);
        INDEXER_SPEED = settingsFile.getDoubleProperty("INDEXER_SPEED", 0.4);

        INTAKE_SPEED = settingsFile.getDoubleProperty("INTAKE_SPEED", 0.8);
        RELEASE_SPEED = settingsFile.getDoubleProperty("RELEASE_SPEED", 0.5);
        RELEASE_NEO_SPEED = settingsFile.getDoubleProperty("RELEASE_NEO_SPEED", 0.5); //hi

        GONDOLA_SPEED = settingsFile.getDoubleProperty("GONDOLA_SPEED", 1);
        CLIMB_SPEED = settingsFile.getDoubleProperty("CLIMB_SPEED", 0.5);

        FLYWHEEL_SPEED = settingsFile.getDoubleProperty("FLYWHEEL_SPEED", 1);

        HOOD_PID_ZONE4 = settingsFile.getIntProperty("HOOD_PID_ZONE4", 36);
        HOOD_PID_ZONE2 = settingsFile.getIntProperty("HOOD_PID_ZONE2", 28);
        HOOD_PID_ZONE1 = settingsFile.getIntProperty("HOOD_PID_ZONE1", 2);
        HOOD_PID_ZONE3 = settingsFile.getIntProperty("HOOD_PID_ZONE3", 32);

        HOOD_SPEED_DOWN = settingsFile.getDoubleProperty("HOOD_SPEED_DOWN", 0.5);
        HOOD_SPEED_UP = settingsFile.getDoubleProperty("HOOD_SPEED_UP", 0.5);


        FLYWHEEL_SPEED_ZONE4 = settingsFile.getIntProperty("FLYWHEEL_SPEED_ZONE4", 7000);
        FLYWHEEL_SPEED_ZONE2 = settingsFile.getIntProperty("FLYWHEEL_SPEED_ZONE2", 6000);
        FLYWHEEL_SPEED_ZONE1 = settingsFile.getIntProperty("FLYWHEEL_SPEED_ZONE1", 6000);
        FLYWHEEL_SPEED_ZONE3 = settingsFile.getIntProperty("FLYWHEEL_SPEED_ZONE3", 6000);

        LIMELIGHT_ANGLE_FROM_FLAT = settingsFile.getDoubleProperty("LIMELIGHT_ANGLE_FROM_FLAT", 0);
        LIMELIGHT_DISTANCE_FROM_GROUND = settingsFile.getDoubleProperty("LIMELIGHT_DISTANCE_FROM_GROUND", 0);
        TARGET_DEFAULT_HEIGHT = settingsFile.getDoubleProperty("TARGET_DEFAULT_HEIGHT", 0);

    }

    /**
     * Reloads the setting variables. Checks to see if the settingFile has changed, and if so then resets all the dynamic settings
     * to either the default value or to the value on the settings.txt
     * 
     * @author Cool Kornak
     * @since 2018
     */
    public void settingsPeriodic() {
        try {
            settingsFile.reload();
        } catch (NullPointerException e) {
            Timer.delay(0.01);
        }

        if (!settingsActive.equalsIgnoreCase(settingsFile.toString())) {
            System.out.println("reloading settings");
            settingsValueUpdate();
            settingsActive = settingsFile.toString();
        }
    }

}