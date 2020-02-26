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
    private SendableChooser<File> selectedAuto = new SendableChooser<>();

    public Auto(Drive driveSubsystems, Shooter shooterSubsystem, Cartridge cartridgeSubsystem, Intake intakeSubsystem, DrivePID drivePID, TurnPID turnPID, FlywheelPID flywheelPID, AlignPID alignPID, HoodPID hoodPID) {
        autoFile = new AutoFile(driveSubsystems, shooterSubsystem, cartridgeSubsystem,
                intakeSubsystem, drivePID, turnPID, flywheelPID, alignPID, hoodPID);
        ShuffleboardTab autoTab = Shuffleboard.getTab("Auto");
        selectedAuto.setDefaultOption("Move off line", new File("/FILES/auto_moveoffline.txt"));
        selectedAuto.addOption("Test", new File("/FILES/auto_test.txt"));
        autoTab.add("Auto Selector", selectedAuto).withWidget(BuiltInWidgets.kComboBoxChooser);
    }

    public void autoInit() {
        try {
            autoFile.init(selectedAuto.getSelected());
        } catch (Exception e) {
            DriverStation.reportWarning("no auto file detected!", false);
        }
    }

    public void autoPeriodic() {
        autoFile.run();
    }

    public void autoDisabled() {
        autoFile.end();
    }

}