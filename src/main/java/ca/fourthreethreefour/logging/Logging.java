package ca.fourthreethreefour.logging;

import java.io.FileWriter;

import ca.fourthreethreefour.settings.Settings;
import ca.fourthreethreefour.subsystems.Drive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Logging {

    FileWriter file = null;
    String data = Drive.loggingCategories() + "\n";

    public void record() {
        data += Drive.loggingData() + "\n";
    }

    public void write() {
        try {
            file = new FileWriter("/home/lvuser/files/matchData.csv");
            file.write(data);
            file.flush();
          } catch (Exception e) {
            System.out.println(e);
          }
    }

    public static void put(String key, double value) {
        if (Settings.LOGGING_ENABLED) {
            // key.setDouble(value);
            SmartDashboard.putNumber(key, value);
        }
    }
    
    public static void put(String key, String value) {
        if (Settings.LOGGING_ENABLED) {
            // key.setString(value);
            SmartDashboard.putString(key, value);
        }
    }
    
    public static void put(String key, boolean value) {
        if (Settings.LOGGING_ENABLED) {
            // key.setBoolean(value);
            SmartDashboard.putBoolean(key, value);
        }
    }

    public static void log(String str) {
        if (Settings.LOGGING_ENABLED) {
            System.out.println(str);
        }
    }
    
    public static void logf(String format, Object... args) {
        if (Settings.LOGGING_ENABLED) {
            System.out.printf(format + "\n", args);
        }
    }
}