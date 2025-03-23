package Stage1;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class IntakeSubsystem extends SubsystemBase {


    private static DcMotorEx slideMotor;

    private static Servo openClose;
    private static Servo leftRight;
    private static Servo upDown;

    public double kP = 0, kI = 0, kD = 0, kF = 0;

    private static PIDController extendController;


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


    public IntakeSubsystem(HardwareMap hMap){

        //TODO: CHANGE ALL NAMES TO CORRECT VALUES
        slideMotor = hMap.get(DcMotorEx.class, "ISM");

        openClose = hMap.get(Servo.class, "ICS");
        leftRight = hMap.get(Servo.class, "IXS");
        upDown = hMap.get(Servo.class, "IYS");

        extendController = new PIDController(kP, kI, kD);
    }


    public void update(){

        //Update all relevant things
        motorPos = slideMotor.getCurrentPosition();

        //Clamp the target value to not break extension limits
        extensionTarget = Math.max(targetMin, Math.min(targetMax, extensionTarget));

        //Calculate power using built in PIDF class (easy and reliable)
        power = Math.max(-1, Math.min(1, extendController.calculate(motorPos, extensionTarget)));
        power += kF;

        //Set motor(s) to have the power determined by the loop
        slideMotor.setPower(power);

        openClose.setPosition(openCloseTarget);
        leftRight.setPosition(leftRightTarget);
        upDown.setPosition(upDownTarget);

        extendController.setPID(kP, kI, kD);
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

    public void setExtensionTarget(int target){extensionTarget = target;}

    public void TelemetryTesting(MultipleTelemetry tele){

        tele.addData("Slide Motor Encoder ", slideMotor.getCurrentPosition());


        tele.addData("Up/Down Angle Servo Position", upDown.getPosition());
        tele.addData("Left/Right Claw Servo Position", leftRight.getPosition());
        tele.addData("Open/Close Claw Servo Position", openClose.getPosition());


        tele.addData("Extension Target ", extensionTarget);
        tele.addData("Power Output ", power);
        tele.update();


    }


}
