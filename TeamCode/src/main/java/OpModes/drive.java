package OpModes;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import Stage1.IntakeSubsystem;
import Stage2.OuttakeSubsystem;
import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;

@TeleOp(name = "HipsterDrive", group = "Real TeleOP")
public class drive extends LinearOpMode {

    Follower follower;
    IntakeSubsystem intake;
    OuttakeSubsystem outtake;

    int outtakeExtendTarget = 0;
    int intakeExtendTarget = 0;


    @Override
    public void runOpMode() throws InterruptedException {

        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
        intake = new IntakeSubsystem(hardwareMap);
        outtake = new OuttakeSubsystem(hardwareMap);


        while(opModeInInit()){
            //Init Loop

            //Values to keep the robot from snapping
            intake.clawVertCustom(.25);
            outtake.setTargetAngle(.5);


        }

        follower.startTeleopDrive();
        while(isStarted() && !isStopRequested()){
            //run loop
            double deflator = gamepad2.left_bumper ? .5 : 1;

            // TODO ACTUAL CODE
            //Intake
            /*
            if(gamepad2.a){
                intake.clawOpen();
                intake.clawDown();
            }
            else{
                intake.clawClose();
                intake.clawTransfer();
            }
             */
            //First should be perpendicular
            //Second should be parallel
            if(gamepad2.right_bumper){intake.turnClaw(1);}
            else if (gamepad2.left_bumper){intake.turnClaw(0);}

            //TODO Find Close values and update gamepad1's actual code
            //Outtake  ////////////TEMPORARY
            if(gamepad1.b){
                outtake.clawOpen();
            }
            else{outtake.clawClose(gamepad1.right_trigger);}

            //First should be perpendicular
            //Second should be parallel
            if(gamepad2.x){outtake.turnClaw(1);}
            else if (gamepad2.y){outtake.turnClaw(0);}















            outtakeExtendTarget += (int) (Math.pow(gamepad2.right_stick_y, 3) * -24 * deflator);
            intakeExtendTarget += (int) (Math.pow(gamepad2.left_stick_y, 3) * -12 * deflator);

            outtakeExtendTarget = Math.max(OuttakeSubsystem.targetMin, Math.min(OuttakeSubsystem.targetMax, outtakeExtendTarget));
            intakeExtendTarget = Math.max(IntakeSubsystem.targetMin, Math.min(IntakeSubsystem.targetMax, intakeExtendTarget));

            outtake.setExtensionTarget(outtakeExtendTarget);
            intake.setExtensionTarget(intakeExtendTarget);
            intake.update();
            outtake.update();
            follower.setTeleOpMovementVectors(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);
            follower.update();


        }




    }
}
