package Stage1;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import Stage2.OuttakeSubsystem;

@TeleOp(name = "PIDFTesting Intake", group = "Real TeleOP")
@Config
@Disabled
public class PIDFTestIntake extends LinearOpMode {

    Follower follower;

    IntakeSubsystem intake;
    public static int target = 0;

    @Override
    public void runOpMode() throws InterruptedException {

        intake = new IntakeSubsystem(hardwareMap);


        while(opModeInInit()){
            //Init Loop
        }


        while(isStarted() && !isStopRequested()){
            //run loop

            intake.update();
            intake.TelemetryTesting(new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry()));


        }




    }
}
