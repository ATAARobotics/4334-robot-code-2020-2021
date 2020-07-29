package ca.fourthreethreefour.vision;

import ca.fourthreethreefour.settings.Settings;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * Class to communicate with the limelight via Network Tables.
 * 
 * @author Matthew Naruzny
 *
 * */

public class LimeLight {

    private NetworkTable table;
    private NetworkTableEntry tv;
    private NetworkTableEntry tx;
    private NetworkTableEntry ty;
    private NetworkTableEntry ta;
    private NetworkTableEntry tshort;
    private NetworkTableEntry tlong;
    private NetworkTableEntry ledMode;
    private NetworkTableEntry camMode;

    // Following 3 measurements are in Inches
    private double LIMELIGHT_HEIGHT_FROM_GROUND = Settings.LIMELIGHT_DISTANCE_FROM_GROUND;
    private double LIMELIGHT_ANGLE_FROM_FLAT = Settings.LIMELIGHT_ANGLE_FROM_FLAT;
    private double TARGET_DEFAULT_HEIGHT = Settings.TARGET_DEFAULT_HEIGHT;

    public LimeLight(){
        table = NetworkTableInstance.getDefault().getTable("limelight-ata");
        tv = table.getEntry("tv");
        tx = table.getEntry("tx");
        ty = table.getEntry("ty");
        ta = table.getEntry("ta");
        tshort = table.getEntry("tshort");
        tlong = table.getEntry("tlong");
        ledMode =  table.getEntry("ledMode");
        camMode = table.getEntry("camMode");
    }

    /**
     * Returns a number to represent whether the limelight has detected any valid targets.
     * @return Returns 0 if no targets are found or 1 if targets are found.
     */
    public double getTv(){
        return tv.getDouble(2);
    }

    /**
     * Returns the horizontal offset from the crosshair to the target
     * @return Target Horizontal Offset from Crosshair. (Value between -27 and 27)
     */
    public double getTx(){
        return tx.getDouble(0);
    }

    /**
     * Returns the vertical offset from crosshair to target
     * @return Target Vertical Offset from Crosshair. (Value between -20.5 and 20.5)
     */
    public double getTy(){
        return ty.getDouble(0);
    }

    /**
     * Returns percentage representing how much of the image the target(s) cover
     * @return Target Area. (0% of the image to 100% of the image)
     */
    public double getTa(){
        return ta.getDouble(0);
    }

    /**
     * Returns sidelength of shortest side of the fitted bounding box
     * @return Shortest Side in Pixels
     */
    public double getTshort(){
        return tshort.getDouble(0);
    }

    /**
     * Returns sidelength of longest side of the fitted bounding box
     * @return Longest Side in Pixels
     */
    public double getTlong(){
        return tlong.getDouble(0);
    }

    /**
     * Set the limelight LED to follow LED mode set in current pipeline
     */
    public void ledDefault(){
        ledMode.setDouble(0);
    }

    /**
     * Force the limelight LED to on
     */
    public void ledOn(){
        ledMode.setDouble(3);
    }

    /**
     * Force the limelight LED to off
     */
    public void ledOff(){
        ledMode.setDouble(1);
    }

    /**
     * Force the limelight LED to Blink
     */
    public void ledStrobe(){
        ledMode.setDouble(2);
    }

    /**
     * Switch LimeLight between different modes. Takes CameraMode as a parameter to set camera mode.
     * @param mode The CameraMode for the mode to set the camera to.
     * @see CameraMode
     * */
    public void setCameraMode(CameraMode mode) {
        switch (mode) {
            case Drive:
                camMode.setDouble(1);
                ledMode.setDouble(1);
                break;
            case Vision:
                camMode.setDouble(0);
                ledMode.setDouble(3);
                break;
            case Disco:
                camMode.setDouble(1);
                ledMode.setDouble(2);
        }
    }

    /**
     * Calculate the distance between LimeLight and VisionTarget
     * @return Distance between LimeLight and VisionTarget
     */
    public double getDistanceFromTarget(){
        /*
            d = (h2-h1) / tan(a1+a2)
            h1 - LimeLight Height
            h2 - Target Height
            a1 - Limelight Angle from Flat
            a2 - Angle from Limelight to Target
         */

        double verticalFromCamera = (TARGET_DEFAULT_HEIGHT - LIMELIGHT_HEIGHT_FROM_GROUND);
        double angleToTarget = (LIMELIGHT_ANGLE_FROM_FLAT + getTy());

        return (verticalFromCamera / Math.tan(Math.toRadians(angleToTarget)));
    }

    public double getAngleToTarget() {
        return (LIMELIGHT_ANGLE_FROM_FLAT + getTy());
    }

    public enum CameraMode {
        Drive,
        Vision,
        Disco;
    }

}
