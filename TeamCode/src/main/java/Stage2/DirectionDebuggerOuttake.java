package Stage2;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Disabled
@TeleOp(name = "Direction Debugger Outtake", group = "Debugger")
public class DirectionDebuggerOuttake extends LinearOpMode {

    private static DcMotorEx slideMotorRight;
    private static DcMotorEx slideMotorLeft;

    private static Servo angleLeft;
    private static Servo angleRight;
    private static Servo openClose;
    private static Servo leftRight;

    private static OuttakeSubsystem outtake;

    @Override
    public void runOpMode() throws InterruptedException {

        slideMotorRight = hardwareMap.get(DcMotorEx.class, "ORM");
        slideMotorLeft = hardwareMap.get(DcMotorEx.class, "OLM");

        slideMotorRight.setDirection(DcMotorSimple.Direction.FORWARD);
        slideMotorLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        angleLeft = hardwareMap.get(Servo.class, "OLS");
        angleRight = hardwareMap.get(Servo.class, "ORS");
        leftRight = hardwareMap.get(Servo.class, "OXS");
        openClose = hardwareMap.get(Servo.class, "OCS");

        outtake = new OuttakeSubsystem(hardwareMap);

        MultipleTelemetry tele = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        while(opModeInInit()){
            //Init Loop
        }


        while(isStarted() && !isStopRequested()){
            //run loop


            outtake.TelemetryTesting(tele);

        }




    }



}
