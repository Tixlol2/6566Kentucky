package Stage2;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class OuttakeSubsystem extends SubsystemBase {


    private DcMotorEx slideMotorRight;
    private DcMotorEx slideMotorLeft;

    private Servo angleLeft;
    private Servo angleRight;
    private Servo openClose;
    private Servo leftRight;

    public static double kP = 0.01, kI = 0, kD = 0, kF = 0.05;

    private PIDController extendController;


    static double ticks_per_rotation_ext = 537.7;

    private static final double ticks_in_inch = ticks_per_rotation_ext / (112 / 25.4);

    public static int extensionTarget = 0;

    private int motorPos = 0;

    public static int targetMin = 0;
    public static int targetMax = 1000;

    private double power = 0;

    public static double openCloseTarget = 0;
    public static double leftRightTarget = 0;
    public static double angleTarget = 0;


    public OuttakeSubsystem(HardwareMap hMap){


        slideMotorRight = hMap.get(DcMotorEx.class, "ORM");
        slideMotorLeft = hMap.get(DcMotorEx.class, "OLM");

        slideMotorRight.setDirection(DcMotorSimple.Direction.FORWARD);
        slideMotorLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        angleLeft = hMap.get(Servo.class, "OLS");
        angleRight = hMap.get(Servo.class, "ORS");
        leftRight = hMap.get(Servo.class, "OXS");
        openClose = hMap.get(Servo.class, "OCS");

        extendController = new PIDController(kP, kI, kD);

    }


    public void update(){

        //Update all relevant things
        motorPos = slideMotorRight.getCurrentPosition();

        //Clamp the target value to not break extension limits
        extensionTarget = Math.max(targetMin, Math.min(targetMax, extensionTarget));

        //Calculate power using built in PIDF class (easy and reliable)
        power = extendController.calculate(motorPos, extensionTarget);
        power += kF;
        power = Math.max(-1, Math.min(1, power));
        //Set motor(s) to have the power determined by the loop
        slideMotorRight.setPower(power);
        slideMotorLeft.setPower(power);

        openClose.setPosition(openCloseTarget);
        angleLeft.setPosition(1-angleTarget);
        angleRight.setPosition(angleTarget);
        leftRight.setPosition(leftRightTarget);

        extendController.setPID(kP, kI, kD);

    }

    public void clawClose(double temp){openCloseTarget = temp;} //////Finding
    public void clawOpen(){
        openCloseTarget = .1;
    }
    public void turnClaw(double pos){leftRightTarget = pos;}
    public void setTargetAngle(double position){angleTarget = position;}
    public void setExtensionTarget(int target){extensionTarget = target;}

    public void TelemetryTesting(MultipleTelemetry tele){

        tele.addData("Left Motor Encoder ", slideMotorLeft.getCurrentPosition());
        tele.addData("Right Motor Encoder ", slideMotorRight.getCurrentPosition());

        tele.addData("Left Angle Servo Position ", angleLeft.getPosition());
        tele.addData("Right Angle Servo Position ", angleRight.getPosition());
        tele.addData("Left/Right Claw Servo Position ", leftRight.getPosition());
        tele.addData("Open/Close Claw Servo Position ", openClose.getPosition());

        tele.addData("Extension Target ", extensionTarget);
        tele.addData("Power Output ", power);
        tele.update();


    }

    public void Transfer(){}


}
