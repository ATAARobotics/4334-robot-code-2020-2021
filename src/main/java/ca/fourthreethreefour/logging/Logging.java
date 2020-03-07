package ca.fourthreethreefour.logging;

import java.io.FileWriter;

import ca.fourthreethreefour.settings.Settings;
import ca.fourthreethreefour.subsystems.Cartridge;
import ca.fourthreethreefour.subsystems.Drive;
import ca.fourthreethreefour.subsystems.Shooter;
import ca.fourthreethreefour.subsystems.pid.FlywheelPID;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Logging {

    Shooter shooterSubsystem = null;
    FlywheelPID flywheelPID = null;
    Cartridge cartridgeSubsystem = null;

    int delay = 0;

    public Logging(Shooter shooterSubsystem, FlywheelPID flywheelPID, Cartridge cartridgeSubsystem) {
        this.shooterSubsystem = shooterSubsystem;
        this.flywheelPID = flywheelPID;
        this.cartridgeSubsystem = cartridgeSubsystem;
    }

    FileWriter file = null;
    String data =  "rpm, setpoint, on target, angle, indexer sensor, indexer value, end sensor, end value, start sensor, start value, match time, match number: " + DriverStation.getInstance().getMatchNumber() +"\n";

    public void record() {
        if (delay >= 5) {
            data += shooterSubsystem.getRPM() + ", " + flywheelPID.getController().getSetpoint() + ", " + flywheelPID.getController().atSetpoint() + ", " + shooterSubsystem.getEncoder() + ", " + cartridgeSubsystem.indexerSensor() + ", " + cartridgeSubsystem.indexerGet() + ", " + cartridgeSubsystem.cartridgeEnd() + ", " + cartridgeSubsystem.endGet() + ", " + cartridgeSubsystem.cartridgeStart() + ", " + cartridgeSubsystem.startGet() + ", " + DriverStation.getInstance().getMatchTime() + "\n";    
            delay = 0;
        } else {
            delay++;
        }
    }

    public void write() {
        try {
            file = new FileWriter("/FILES/matchData.csv");
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

    public static void log(double str) {
        if (Settings.LOGGING_ENABLED) {
            System.out.println(str);
        }
    }

    public static void log(int str) {
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