package Stage1;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Direction Debugger Intake", group = "Debugger")
public class DirectionDebuggerIntake extends LinearOpMode {

    private static DcMotorEx slideMotor;
    private static Servo upDown;
    private static Servo openClose;
    private static Servo leftRight;



    @Override
    public void runOpMode() throws InterruptedException {

        slideMotor = hardwareMap.get(DcMotorEx.class, "ORM");

        slideMotor.setDirection(DcMotorSimple.Direction.FORWARD);


        upDown = hardwareMap.get(Servo.class, "IYS");
        leftRight = hardwareMap.get(Servo.class, "IXS");
        openClose = hardwareMap.get(Servo.class, "ICS");

        MultipleTelemetry tele = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        while(opModeInInit()){
            //Init Loop
        }


        while(isStarted() && !isStopRequested()){
            //run loop
            tele.addData("Slide Motor Encoder", slideMotor.getCurrentPosition());

            tele.addData("Up/Down Angle Servo Position", upDown.getPosition());
            tele.addData("Left/Right Claw Servo Position", leftRight.getPosition());
            tele.addData("Open/Close Claw Servo Position", openClose.getPosition());
            tele.update();

        }




    }
}
