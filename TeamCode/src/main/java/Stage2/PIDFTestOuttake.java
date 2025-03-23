package Stage2;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import Stage1.IntakeSubsystem;
import Stage2.OuttakeSubsystem;
import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;

@TeleOp(name = "PIDFTesting Outtake", group = "Real TeleOP")
@Config
@Disabled
public class PIDFTestOuttake extends LinearOpMode {

    Follower follower;
    IntakeSubsystem intake;
    OuttakeSubsystem outtake;
    public static int target = 0;

    @Override
    public void runOpMode() throws InterruptedException {

        outtake = new OuttakeSubsystem(hardwareMap);


        while(opModeInInit()){
            //Init Loop
        }


        while(isStarted() && !isStopRequested()){
            //run loop
            //outtake.setExtensionTarget(target);
            outtake.update();
            outtake.TelemetryTesting(new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry()));


        }




    }
}
