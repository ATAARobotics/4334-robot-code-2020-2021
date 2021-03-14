package ca.fourthreethreefour.auto;

import java.io.File;

import ca.fourthreethreefour.subsystems.Cartridge;
import ca.fourthreethreefour.subsystems.Drive;
import ca.fourthreethreefour.subsystems.Intake;
import ca.fourthreethreefour.subsystems.Shooter;
import ca.fourthreethreefour.subsystems.pid.AlignPID;
import ca.fourthreethreefour.subsystems.pid.DrivePID;
import ca.fourthreethreefour.subsystems.pid.FlywheelPID;
import ca.fourthreethreefour.subsystems.pid.HoodPID;
import ca.fourthreethreefour.subsystems.pid.TurnPID;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class Auto {
    private AutoFile autoFile = null;
    private SendableChooser<File> selectedAuto = new SendableChooser<>(); // A sendablechooser is a list of possible options, all of a set type, that can be selected from and used.

    public Auto(Drive driveSubsystems, Shooter shooterSubsystem, Cartridge cartridgeSubsystem, Intake intakeSubsystem, DrivePID drivePID, TurnPID turnPID, FlywheelPID flywheelPID, AlignPID alignPID, HoodPID hoodPID) {
        autoFile = new AutoFile(driveSubsystems, shooterSubsystem, cartridgeSubsystem,
                intakeSubsystem, drivePID, turnPID, flywheelPID, alignPID, hoodPID); // Sets up an autoFile object with all the required subsystems passed in.
        ShuffleboardTab autoTab = Shuffleboard.getTab("Auto"); // Creates a tab on shuffleboard called Auto
        selectedAuto.setDefaultOption("Move off line", new File("/FILES/auto_moveoffline.txt")); // Creates a default option, and additional options.
        selectedAuto.addOption("Test", new File("/FILES/auto_test.txt")); // Each option wanted needs to be manually added.
        selectedAuto.addOption("Shoot then move towards driverstation", new File("/FILES/auto_shootonline.txt"));
        selectedAuto.addOption("TestCode", new File("/FILES/test_code.txt")); // new
        selectedAuto.addOption("Shoot then move away from driverstation", new File("/FILES/auto_shootmovebackwards.txt" ));
        selectedAuto.addOption("Alliance", new File("/FILES/auto_alliance.txt"));
        autoTab.add("Auto Selector", selectedAuto).withWidget(BuiltInWidgets.kComboBoxChooser); // Creates a widget on the tab that is a selector box with all these options.
    }

    public void autoInit() {
        System.out.println("AUTO PATH SELECTED: " + selectedAuto.getSelected().toString()); // Logging purposes, and for driveteam reference
        try { // Tries to run the main initial loop of the autoFile. If isn't able to find it, reports the issues
            autoFile.init(selectedAuto.getSelected());
        } catch (Exception e) {
            DriverStation.reportWarning("No auto file detected!", false);
            try {
                autoFile.init(new File("/FILES/auto_moveoffline.txt")); // As a precaution, we tell it to try to run with an exact file. If it can't be found, reports the error.
            } catch (Exception f) {
                DriverStation.reportError("DEFAULT AUTO NOT FOUND", true); // If it can't be found, it could be that the files are wiped, which would be concerning.
            }
        }
    }

    public void autoPeriodic() {
        autoFile.run(); // Activates the run loop of the autoFile
    }

    public void autoDisabled() {
        autoFile.end(); // Activates the end loop of the autoFile
    }

}