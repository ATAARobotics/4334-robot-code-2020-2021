package ca.fourthreethreefour.auto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import ca.fourthreethreefour.auto.commands.DriveBlind;
import ca.fourthreethreefour.auto.commands.Print;
import ca.fourthreethreefour.auto.commands.Turn;
import ca.fourthreethreefour.subsystems.Drive;
import ca.fourthreethreefour.subsystems.pid.TurnPID;
import edu.wpi.first.wpilibj2.command.Command;

public class AutoFile {
    private Drive driveSubsystem = null;
    private TurnPID turnPID = null;
    private Vector<Entry> commandEntries = new Vector<>();

    private Vector<Command> queue = new Vector<>();
    private Vector<Boolean> hasRun = new Vector<>();
    private Vector<Integer> state = new Vector<>();

    public static class Entry {
        final String e_key;
        final int e_state;
        final String[] e_arguments;
        static final int CONCURRENT = 0;
        static final int SEQUENTIAL = 1;

        public Entry(String key, int state, String[] arguments) {
            this.e_key = key;
            this.e_state = state;
            this.e_arguments = arguments;
        }
    }

    public AutoFile(File file, Drive driveSubsystem, TurnPID turnPID) throws IOException {
        this.driveSubsystem = driveSubsystem;
        this.turnPID = turnPID;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String currentLine;
        Vector<String> contents = new Vector<>();
        while ((currentLine = bufferedReader.readLine()) != null) {
            contents.addElement(currentLine);
        }
        bufferedReader.close();

        commandEntries.clear();
        for (int i = 0; i < contents.size(); i++) {
            final int state;
            if (contents.get(i).charAt(0) == '!') {
                state = Entry.CONCURRENT;
                contents.setElementAt(contents.get(i).substring(1), i);
            } else {
                state = Entry.SEQUENTIAL;
            }
            String key = contents.get(i).substring(0, contents.get(i).indexOf("(")).toLowerCase();
            contents.setElementAt(
                    contents.get(i).substring(contents.get(i).indexOf("(") + 1, contents.get(i).length() - 1), i);
            String[] args = contents.get(i).split(",");
            commandEntries.addElement(new Entry(key, state, args));
        }
    }

    public void init() {
        queue.clear();
        hasRun.clear();
        state.clear();
        for (int i = 0; i < commandEntries.size(); i++) {
            Entry entry = commandEntries.elementAt(i);
            queue.addElement(selectCommand(entry.e_key, entry.e_arguments));
            hasRun.addElement(false);
            state.addElement(entry.e_state);
        }
    }

    private int queuePosition = 0;
    private int finishedCheck = 0;

    public void run() {
        if (queuePosition < queue.size()) {
            if (!hasRun.get(queuePosition)) {
                queue.get(queuePosition).schedule();
                hasRun.set(queuePosition, true);
            }
            if (state.get(queuePosition) == Entry.SEQUENTIAL) {
                if (finishedCheck <= queuePosition) {
                    if (queue.get(finishedCheck).isFinished()) {
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

    public void end() {
        for (Command command : queue) {
            if (!command.isFinished()) {
                command.cancel();
            }
        }
    }

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
            case "turn":
                double angle = Double.parseDouble(args[0]);
                timeout = args.length > 1 ? Double.parseDouble(args[1]) : 5;
                command = new Turn(driveSubsystem, turnPID, angle).withTimeout(timeout);
                return command;
            default:
                throw new Error(key + " is not a valid command!");
        }
    }
}