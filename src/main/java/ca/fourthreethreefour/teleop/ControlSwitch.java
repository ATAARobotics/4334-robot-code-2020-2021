// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package ca.fourthreethreefour.teleop;

import ca.fourthreethreefour.settings.Settings;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;

/** Add your docs here. */
public class ControlSwitch {

    private XboxController controllerDriver = null;
    private XboxController controllerOperator = null;
    private DriverControlLayouts driverControlLayouts = DriverControlLayouts.NONE;
    private OperatorControlLayouts operatorControlLayouts = OperatorControlLayouts.NONE;

    public ControlSwitch(){
        controllerDriver = new XboxController(Settings.CONTROLLER_DRIVER_PORT);
        controllerOperator = new XboxController(Settings.CONTROLLER_OPERATOR_PORT);

    }
    
    public boolean driveSuddenStop(){
        switch (driverControlLayouts){
            //case NIK:
            //return controllerDriver.getStickButton(Hand.kRight);
            //button and hand can be modified etc
            default:
                return controllerDriver.getStickButton(Hand.kRight);
        }
    }
    
    public double driveMoveForwardBack(){
        switch (driverControlLayouts){
            //case NIK:
            //return controllerDriver.getY(Hand.kLeft);
            //button and hand can be modified etc
            default:
                return controllerDriver.getY(Hand.kLeft);
        }
    }

    public double driveTurnLeftRight(){
        switch (driverControlLayouts){
            //case NIK:
            //return controllerDriver.getX(Hand.kLeft);
            //button and hand can be modified etc
            default:
                return controllerDriver.getX(Hand.kRight);
        }
    }

    public double intakeIn(){
        switch (driverControlLayouts){
            //case NIK:
            //return controllerDriver.getY(Hand.kLeft);
            //button and hand can be modified etc
            default:
                return controllerDriver.getTriggerAxis(Hand.kRight);
        }
    }

    public double intakeOut(){
        switch (driverControlLayouts){
            //case NIK:
            //return controllerDriver.getY(Hand.kLeft);
            //button and hand can be modified etc
            default:
                return controllerDriver.getTriggerAxis(Hand.kLeft);
        }
    }

    public boolean driveSwitchGears(){
        switch (driverControlLayouts){
            //case NIK:
            //return controllerDriver.getY(Hand.kLeft);
            //button and hand can be modified etc
            default:
                return controllerDriver.getStickButton(Hand.kLeft);
        }
    }

    public boolean hoodAdjust(){
        switch (driverControlLayouts){
            //case NIK:
            //return controllerDriver.getY(Hand.kLeft);
            //button and hand can be modified etc
            default:
                return controllerDriver.getPOV();
        }
    }

    public boolean flywheelAdjustUp(){
        switch (driverControlLayouts){
            //case NIK:
            //return controllerDriver.getY(Hand.kLeft);
            //button and hand can be modified etc
            default:
                return controllerDriver.getBumperPressed(Hand.kLeft);
        }
    }

    public boolean flywheelAdjustDown(){
        switch (driverControlLayouts){
            //case NIK:
            //return controllerDriver.getY(Hand.kLeft);
            //button and hand can be modified etc
            default:
                return controllerDriver.getBumperPressed(Hand.kRight);
        }
    }

    public boolean autoAlign(){
        switch (driverControlLayouts){
            //case NIK:
            //return controllerDriver.getY(Hand.kLeft);
            //button and hand can be modified etc
            default:
                return controllerDriver.getXButton();
        }
    }

    public double hoodAdjustCustom(){
        switch (operatorControlLayouts){
            //case NIK:
            //return controllerDriver.getStickButton(Hand.kRight);
            //button and hand can be modified etc
            default:
                return controllerOperator.getY(Hand.kLeft);
        }
    }

    public boolean operatorManualOverride(){
        switch (operatorControlLayouts){
            //case NIK:
            //return controllerDriver.getStickButton(Hand.kRight);
            //button and hand can be modified etc
            default:
                return controllerOperator.getXButton();
        }
    }

    public boolean autoShoot(){
        switch (operatorControlLayouts){
            //case NIK:
            //return controllerDriver.getStickButton(Hand.kRight);
            //button and hand can be modified etc
            default:
                return controllerOperator.getYButton();
        }
    }

    public boolean cartrigeStop(){
        switch (operatorControlLayouts){
            //case NIK:
            //return controllerDriver.getStickButton(Hand.kRight);
            //button and hand can be modified etc
            default:
                return controllerOperator.getBackButtonPressed();
        }
    }

    public boolean gondolaControls(){
        switch (operatorControlLayouts){
            //case NIK:
            //return controllerDriver.getStickButton(Hand.kRight);
            //button and hand can be modified etc
            default:
                return controllerOperator.getPOV();
        }
    }

    public boolean cartidgeIn(){
        switch (operatorControlLayouts){
            //case NIK:
            //return controllerDriver.getStickButton(Hand.kRight);
            //button and hand can be modified etc
            default:
                return controllerOperator.getBumper(Hand.kLeft);
        }
    }

    public boolean cartidgeOut(){
        switch (operatorControlLayouts){
            //case NIK:
            //return controllerDriver.getStickButton(Hand.kRight);
            //button and hand can be modified etc
            default:
                return controllerOperator.getBumper(Hand.kRight);
        }
    }

    public boolean indexerIn(){
        switch (operatorControlLayouts){
            //case NIK:
            //return controllerDriver.getStickButton(Hand.kRight);
            //button and hand can be modified etc
            default:
                return controllerOperator.getBButton();
        }
    }

    public boolean indexerOut(){
        switch (operatorControlLayouts){
            //case NIK:
            //return controllerDriver.getStickButton(Hand.kRight);
            //button and hand can be modified etc
            default:
                return controllerOperator.getAButton();
        }
    }

    public boolean climbControls(){
        switch (operatorControlLayouts){
            //case NIK:
            //return controllerDriver.getStickButton(Hand.kRight);
            //button and hand can be modified etc
            default:
                return controllerOperator.getStartButton();
        }
    }

    public boolean intakeUpDown(){
        switch (operatorControlLayouts){
            //case NIK:
            //return controllerDriver.getStickButton(Hand.kRight);
            //button and hand can be modified etc
            default:
                return controllerOperator.getY(Hand.kRight);
        }
    }

    

    private enum DriverControlLayouts{

        NIK,
        ALEX,
        NONE
    }

    private enum OperatorControlLayouts{

    NIK,
    ALEX,
    NONE
    }

}


//Replace all buttons with controlSwitch functions
//Create defaults in each function
//some will be doubles
//makes sure the same buttons are all the same functions
//don't creat specific control schemes yet
//Replace all controllerDrivers and Operators with controller.
//Replace all controller.'s buttons with enum functions
