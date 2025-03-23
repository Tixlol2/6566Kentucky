package Stage1;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class intakeSubsystem extends SubsystemBase {


    private static DcMotorEx slideMotor;

    private static Servo openClose;
    private static Servo leftRight;
    private static Servo upDown;

    private double kP, kI, kD, kF;

    private static PIDFController extendController;


    static double ticks_per_rotation_ext = 537.7;

    private static final double ticks_in_inch = ticks_per_rotation_ext / (112 / 25.4);

    private static int extensionTarget = 0;

    private static int motorPos = 0;

    private static int targetMin = 0;
    private static int targetMax = 1000;

    private static double power = 0;

    private static double openCloseTarget;
    private static double leftRightTarget;
    private static double upDownTarget;


    public intakeSubsystem(HardwareMap hMap){

        //TODO: CHANGE ALL NAMES TO CORRECT VALUES
        slideMotor = hMap.get(DcMotorEx.class, "NAME");

        openClose = hMap.get(Servo.class, "NAME");
        leftRight = hMap.get(Servo.class, "NAME");
        upDown = hMap.get(Servo.class, "NAME");

    }


    public void update(){

        //Update all relevant things
        motorPos = slideMotor.getCurrentPosition();

        //Clamp the target value to not break extension limits
        extensionTarget = Math.max(targetMin, Math.min(targetMax, extensionTarget));

        //Calculate power using built in PIDF class (easy and reliable)
        power = Math.max(-1, Math.min(1, extendController.calculate(motorPos, extensionTarget)));

        //Set motor(s) to have the power determined by the loop
        slideMotor.setPower(power);

        openClose.setPosition(openCloseTarget);
        leftRight.setPosition(leftRightTarget);
        upDown.setPosition(upDownTarget);

    }

    public void intakeClawClose(){
        openCloseTarget = 1;
    }
    public void intakeClawOpen(){
        openCloseTarget = 0;
    }
    public void intakeClawUp(){
        upDownTarget = 1;
    }
    public void intakeClawDown(){
        upDownTarget = 0;
    }
    public void intakeClawParallel(){
        upDownTarget = 1;
    }
    public void intakeClawPerpendicular(){
        upDownTarget = 0;
    }


}
