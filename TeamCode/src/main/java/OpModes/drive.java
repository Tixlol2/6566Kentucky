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

    follower = new Follower(hardwareMap);
    intake = new IntakeSubsystem(hardwareMap);
    outtake = new OuttakeSubsystem(hardwareMap);


    while (opModeInInit()) {
        //Init Loop

        //Values to keep the robot from snapping
        intake.clawVertCustom(.25);
        intake.turnClaw(.48);
        outtake.turnClaw(.472);
        outtake.setTargetAngle(.5);

    }

    follower.startTeleopDrive();
    while (isStarted() && !isStopRequested()) {
        //run loop
        double deflator = gamepad2.left_bumper ? .5 : 1;

        //Intake
        if (gamepad1.a) {
            intake.clawDown();
        } else if (gamepad1.b) {
            intake.clawTransfer();
        }

        if (gamepad1.y) {
            intake.clawClose();
        } else if (gamepad1.x) {
            intake.clawOpen();
        } else if (gamepad1.dpad_up) {
            intake.clawCloseTight();
        }

        if (gamepad1.dpad_left) {
            intake.turnClawAdd(-.01);
        } else if (gamepad1.dpad_right) {
            intake.turnClawAdd(+.01);
        } else if (gamepad1.dpad_down) {
            intake.turnClaw(.23);
        }

        if (gamepad1.right_trigger > 0) {
            intake.sliding(gamepad1.right_trigger > 0, 1 * gamepad1.right_trigger);
        }
        else if (gamepad1.left_trigger > 0) {
            intake.sliding(gamepad1.left_trigger > 0, -1 * gamepad1.left_trigger);
        } else {
            intake.sliding(false, 0);
        }

        //Outtake

        //First should be perpendicular
        //Second should be parallel
        //if(gamepad2.x){outtake.turnClaw(1);}
        //else if (gamepad2.y){outtake.turnClaw(0);}

        if (gamepad2.dpad_up) {
            outtakeExtendTarget = 3000;
        } else if (gamepad2.dpad_down) {
            outtakeExtendTarget = 715;
        } else if (gamepad2.dpad_left) {
            outtakeExtendTarget = 0;
        }

        if (gamepad2.right_trigger > 0) {
            outtake.clawClose();
        } else if (gamepad2.left_trigger > 0) {
            outtake.clawOpen();
        }

        if (gamepad2.dpad_right) {outtake.setTargetAngle(.5);
        } else if (gamepad2.y) {outtake.setTargetAngle(.68);
        } else if (gamepad2.a) {outtake.setTargetAngle(.015);}

        if (gamepad2.b) {outtake.turnClaw(.472);}
        else if (gamepad2.x) {outtake.turnClaw(.123);}

        if (gamepad2.right_bumper) {outtake.updateExtensionTarget();}


        telemetry.addLine(String.valueOf(outtake.getTwist()));


        //TODO: Repalce sticks with triggers
        //outtakeExtendTarget += (int) (Math.pow(gamepad2.right_stick_y, 3) * -24 * deflator);
        //intakeExtendTarget += (int) (Math.pow(gamepad2.left_stick_y, 3) * -12 * deflator);

        outtakeExtendTarget = Math.max(OuttakeSubsystem.targetMin, Math.min(OuttakeSubsystem.targetMax, outtakeExtendTarget));
        //intakeExtendTarget = Math.max(IntakeSubsystem.targetMin, Math.min(IntakeSubsystem.targetMax, intakeExtendTarget));

        outtake.setExtensionTarget(outtakeExtendTarget);
        //intake.setExtensionTarget(intakeExtendTarget);
        intake.update();
        outtake.update();
        follower.setTeleOpMovementVectors(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);
        follower.update();

        telemetry.addLine(String.valueOf(outtakeExtendTarget));
        telemetry.update();
        }
    }
}

