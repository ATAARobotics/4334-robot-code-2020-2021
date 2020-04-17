package ca.fourthreethreefour.auto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

import ca.fourthreethreefour.auto.commands.Aim;
import ca.fourthreethreefour.auto.commands.AutoAim;
import ca.fourthreethreefour.auto.commands.AutoAlign;
import ca.fourthreethreefour.auto.commands.Crazy;
import ca.fourthreethreefour.auto.commands.DriveBlind;
import ca.fourthreethreefour.auto.commands.DriveStraight;
import ca.fourthreethreefour.auto.commands.IntakeMove;
import ca.fourthreethreefour.auto.commands.Load;
import ca.fourthreethreefour.auto.commands.Print;
import ca.fourthreethreefour.auto.commands.Shoot;
import ca.fourthreethreefour.auto.commands.Stop;
import ca.fourthreethreefour.auto.commands.Turn;
import ca.fourthreethreefour.auto.commands.Wait;
import ca.fourthreethreefour.auto.commands.WaitUntil;
import ca.fourthreethreefour.logging.Logging;
import ca.fourthreethreefour.subsystems.Cartridge;
import ca.fourthreethreefour.subsystems.Drive;
import ca.fourthreethreefour.subsystems.Intake;
import ca.fourthreethreefour.subsystems.Shooter;
import ca.fourthreethreefour.subsystems.pid.AlignPID;
import ca.fourthreethreefour.subsystems.pid.DrivePID;
import ca.fourthreethreefour.subsystems.pid.FlywheelPID;
import ca.fourthreethreefour.subsystems.pid.HoodPID;
import ca.fourthreethreefour.subsystems.pid.TurnPID;
import edu.wpi.first.wpilibj2.command.Command;

/**
 * Designed for easy development of auto routines for the autonomous mode.
 * Takes a specified .txt file, parses it, and creates a queue of commands, then runs it during autonomous mode.
 * 
 * @author Kulkinz - 2020 Programming Lead
 * - Based off of previous work from ATALibJ-era 4334.
 */
public class AutoFile {

    // Subsystems for the respective commands.
    private Drive driveSubsystem = null;
    private Shooter shooterSubsystem = null;
    private Cartridge cartridgeSubsystem = null;
    private Intake intakeSubsystem = null;
    private DrivePID drivePID = null;
    private TurnPID turnPID = null;
    private FlywheelPID flywheelPID = null;
    private AlignPID alignPID = null;
    private HoodPID hoodPID = null;
  
    // Vector list to be used for commands.
    private Vector<Entry> commands = new Vector<>();

    // Passes in all the respective subsystem objects at initialization.
    public AutoFile(Drive driveSubsystem, Shooter shooterSubsystem, Cartridge cartridgeSubsystem, Intake intakeSubsystem, DrivePID drivePID, TurnPID turnPID, FlywheelPID flywheelPID, AlignPID alignPID, HoodPID hoodPID) {
        this.driveSubsystem = driveSubsystem;
        this.shooterSubsystem = shooterSubsystem;
        this.cartridgeSubsystem = cartridgeSubsystem;
        this.intakeSubsystem = intakeSubsystem;
        this.drivePID = drivePID;
        this.turnPID = turnPID;
        this.flywheelPID = flywheelPID;
        this.alignPID = alignPID;
        this.hoodPID = hoodPID;
    }

    /**
     * Creates a command object.
     * Takes a key, finds the related command, creates a command object with the specified arguments, and returns the object. 
     * @param key - the command to be selected
     * @param args - the set of arguments to be used in construction of a command object
     * @return An command extended object to be ran.
     */
    public Command selectCommand(String key, String[] args) {
        Command command;
        double timeout;
        switch (key) {
            case "print":
                String str = args[0];
                command = new Print(str);
                return command;
            case "driveblind":
                double leftSpeed = Double.parseDouble(args[0]);
                double rightSpeed = Double.parseDouble(args[1]);
                timeout = args.length > 2 ? Double.parseDouble(args[2]) : 5;
                command = new DriveBlind(driveSubsystem, leftSpeed, rightSpeed).withTimeout(timeout);
                return command;
            case "drivestraight":
                double distance = Double.parseDouble(args[0]);
                timeout = args.length > 1 ? Double.parseDouble(args[1]) : 5;
                command = new DriveStraight(driveSubsystem, drivePID, turnPID, distance).withTimeout(timeout);
                return command;
            case "turn":
                double turnAngle = Double.parseDouble(args[0]);
                timeout = args.length > 1 ? Double.parseDouble(args[1]) : 5;
                command = new Turn(driveSubsystem, turnPID, turnAngle).withTimeout(timeout);
                return command;
            case "wait":
                timeout = Double.parseDouble(args[0]);
                command = new Wait().withTimeout(timeout);
                return command;
            case "waituntil":
                double time = Double.parseDouble(args[0]);
                command = new WaitUntil(driveSubsystem, time);
                return command;
            case "stop":
                command = new Stop(driveSubsystem, drivePID, turnPID);
                return command;
            case "shoot":
                double RPM = Double.parseDouble(args[0]);
                timeout = Double.parseDouble(args[1]);
                command = new Shoot(shooterSubsystem, cartridgeSubsystem, flywheelPID, RPM).withTimeout(timeout);
                return command;
            case "load":
                timeout = Double.parseDouble(args[0]);
                command = new Load(cartridgeSubsystem, intakeSubsystem).withTimeout(timeout);
                return command;
            case "autoalign":
                timeout = Double.parseDouble(args[0]);
                command = new AutoAlign(alignPID, driveSubsystem).withTimeout(timeout);
                return command;
            case "aim":
                timeout = Double.parseDouble(args[0]);
                double hoodAngle = Double.parseDouble(args[1]);
                command = new Aim(hoodPID, shooterSubsystem, hoodAngle);
                return command;
            case "autoaim":
                timeout = Double.parseDouble(args[0]);
                command = new AutoAim(hoodPID, shooterSubsystem);
                return command;
            case "intakemove":
                String direction = args[0];
                timeout = Double.parseDouble(args[1]);
                command = new IntakeMove(intakeSubsystem, direction);
                return command;
            case "crazy": 
                double aRPM = Double.parseDouble(args[0]);
                timeout = Double.parseDouble(args[1]);
                command = new Crazy(shooterSubsystem, cartridgeSubsystem, flywheelPID, aRPM, intakeSubsystem);
                return command;
            default:
                throw new Error(key + " is not a valid command!");
        }
    }

    /**
     * Used to store commands. Constructed with the key, state, and arguments. 
     * Contains the command and if the command has ran.
     * @implNote Concurrent specifies that the command runs alongside the next command. 
     * There is no limit to how many can be set to concurrent.
     * @implNote Sequential specifies that the command should not run alongside the next command.
     * Once it hits a sequential command, it runs all the commands including the sequential one.
     */
    public static class Entry {
        final String e_key;
        final int e_state;
        final String[] e_arguments;
        Command e_command;
        Boolean e_hasRun = false;
        static final int CONCURRENT = 0;
        static final int SEQUENTIAL = 1;

        public Entry(String key, int state, String[] arguments) {
            this.e_key = key;
            this.e_state = state;
            this.e_arguments = arguments;

            Logging.log(key + " " + state + " " + Arrays.toString(arguments));
        }
    }

    /**
     * Parsed the auto file. Takes a txt file, and creates a queue of commands for use.
     * @param file - specified txt file containing auto data.
     * @throws IOException
     */
    public void init(File file)  throws IOException {
        /*
            Uses a BufferedReader object to process the txt file. Takes the current line, put it into the
            currentLine variable, and add the line into the contents Vector. Then closes the reader once
            it has processed all the lines.
        */
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String currentLine;
        Vector<String> contents = new Vector<>();
        while ((currentLine = bufferedReader.readLine()) != null) {
            contents.addElement(currentLine);
        }
        bufferedReader.close();

        // Clears any existing data in the commands vector, then runs through each entry in the contents vector.
        commands.clear();
        Logging.logf("Size: ",contents.size());
        for (int i = 0; i < contents.size(); i++) {
            final int state;

            /* Checks to see if it has an '!'. If it does, labels it at a concurrent command, and gets rid
            of the character in the entry. Else sets it as sequential. */
            if (contents.get(i).charAt(0) == '!') {
                state = Entry.CONCURRENT;
                contents.setElementAt(contents.get(i).substring(1), i);
            } else {
                state = Entry.SEQUENTIAL;
            }

            // Sets a string variable with the contents of the entry, substringed to only include everything before the first bracket. 
            String key = contents.get(i).substring(0, contents.get(i).indexOf("(")).toLowerCase();
            
            // Sets a string array variable with contents in the brackets, split up via any commands in the arguments.
            String[] args = contents.get(i).substring(contents.get(i).indexOf("(") + 1, contents.get(i).length() - 1).split(",");

            // Creates a new Entry object containing the key, state, and arguments, to be used when running commands.
            commands.addElement(new Entry(key, state, args));
        }

        // Runs for each created command. Takes the entry object, and sets the command based on the parsed data.
        for (int i = 0; i < commands.size(); i++) {
            Entry entry = commands.elementAt(i);
            entry.e_command = selectCommand(entry.e_key, entry.e_arguments);
        }

        Logging.log("Auto File Parsed");
        // Clears all the unparsed data.
        contents.clear();
    }

    // Creates variables for use in running with starting values of 0.
    private int queuePosition = 0;
    private int finishedCheck = 0;

    /**
     * Runs through the queue of commands.
     * @implNote Designed to constantly be ran through during periodic period. No
     * while or for loops as they hold up the program and cause technical issues and
     * potential problems.
     */
    public void run() {
        // If the current position is still less than the total amount of commands.
        if (queuePosition < commands.size()) {
            // If the command hasn't ran, schedules the command to run and sets the boolean flag hasRun as true.
            if (!commands.get(queuePosition).e_hasRun) {
                commands.get(queuePosition).e_command.schedule();
                commands.get(queuePosition).e_hasRun = true;
            }
            /* If the command's state is sequential, ensures that all commands have finished before continuing,
            else it increments the position */
            if (commands.get(queuePosition).e_state == Entry.SEQUENTIAL) {
                /* Checks each command in the queue up to the current position if it has finished. If the command has,
                moves on to the next command to check. If all the commands up to the current position have finished,
                increments the position and continues running. */
                if (finishedCheck <= queuePosition) {
                    if (commands.get(finishedCheck).e_command.isFinished()) {
                        finishedCheck++;
                    }
                } else {
                    queuePosition++;
                }
            } else {
                queuePosition++;
            }
        }
    }

    /**
     * Ends any currently running commands and resetting the process for the next use.
     */
    public void end() {
        // Checks every command. If it is currently running, cancels the command.
        for (int i = 0; i < commands.size(); i++) {
            if (!commands.get(i).e_command.isFinished()) {
                commands.get(i).e_command.cancel();
            }
        }
        // Clears the command vector and resets the variables to 0.
        commands.clear();
        queuePosition = 0;
        finishedCheck = 0;
        Logging.log("Auto file end");
    }
}