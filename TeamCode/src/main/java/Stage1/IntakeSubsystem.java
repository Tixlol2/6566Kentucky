package Stage1;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotor;
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

    public static int targetMin = 0;
    public static int targetMax = 1000;

    private static double power = 0;

    private static double openCloseTarget = .25;
    private static double leftRightTarget = .48;
    private static double upDownTarget;
    private static boolean manual;


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

        /*

        //TODO Uncomment when Noah gets back

        //Clamp the target value to not break extension limits
        extensionTarget = Math.max(targetMin, Math.min(targetMax, extensionTarget));

        //Calculate power using built in PIDF class (easy and reliable)
        power = Math.max(-1, Math.min(1, extendController.calculate(motorPos, extensionTarget)));
        power += kF;

        //Set motor(s) to have the power determined by the loop
        slideMotor.setPower(power);

         extendController.setPID(kP, kI, kD);

         */



        openClose.setPosition(openCloseTarget);
        leftRight.setPosition(leftRightTarget);
        upDown.setPosition(upDownTarget);

    }

    public void clawClose(){openCloseTarget = .58;}
    public void clawCloseTight(){openCloseTarget = .68;}

    public void clawOpen(){openCloseTarget = .25;}
    public void clawTransfer(){upDownTarget = 0;}
    public void clawDown(){upDownTarget = .64;}
    public void turnClaw(double pos){leftRightTarget = Math.max(Math.min(.87, pos), .2);}
    public void turnClawAdd(double plus){turnClaw(leftRightTarget + plus);}
    public void clawVertCustom(double pos){upDownTarget = pos;}
    public int getMotorPos(){return motorPos;}

    //Ethan's Extension Methods
    public void sliding(boolean manual, double pow){
        this.manual = manual;
        if (manual) {
                slideMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                slideMotor.setPower(pow);
        } else {
            slideMotor.setPower(0);
        }
    }

    private static double SmoothPow(double currentPower, double targetPower, double smoothingFactor) {
        smoothingFactor = Math.min(Math.max(smoothingFactor, 0.0), 1);
        return currentPower + (targetPower - currentPower) * smoothingFactor;
    }
    public void runTo(int pos, int start, double pow) {
        double sign;
        if (pos != motorPos) {
            sign = (pos - motorPos) / Math.abs(pos - motorPos);
        }
        else {
            sign = 0;
        }
        if (Math.abs(((double)pos - start) / 2 ) > Math.abs(motorPos - start)) {
            power = SmoothPow(power, pow, .25) * (sign);
        }
        else {
            power = SmoothPow(power, 0, .25) * (sign);
        }
    }

    //Noah's Extension Method
    //public void setExtensionTarget(int target){extensionTarget = target;}

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
