package OpModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.pedropathing.util.Timer;

import Stage1.IntakeSubsystem;
import Stage2.OuttakeSubsystem;
import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;

public class Auton extends OpMode {

    private final Pose startPose = new Pose(9.5, 48, Math.toRadians(180));  // Starting position
    private final Pose scorePose = new Pose(36, 64, Math.toRadians(180)); // Scoring position

    private final Pose pickup1Pose = new Pose(37, 121, Math.toRadians(0)); // First sample pickup
    private final Pose pickup2Pose = new Pose(43, 130, Math.toRadians(0)); // Second sample pickup
    private final Pose pickup3Pose = new Pose(49, 135, Math.toRadians(0)); // Third sample pickup

    private final Pose parkPose = new Pose(12, 24, Math.toRadians(90));    // Parking position


    Timer pathTimer;

    Follower follower;

    int pathState = 0;

    IntakeSubsystem intake;
    OuttakeSubsystem outtake;

    private PathChain startToScore, scoreToPark, parkToScore;


    public void buildPaths(){

        startToScore = follower.pathBuilder()
                .addPath(new BezierLine(new Point(startPose), new Point(scorePose)))
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        scoreToPark = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scorePose), new Point(parkPose)))
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        parkToScore = follower.pathBuilder()
                .addPath(new BezierLine(new Point(parkPose), new Point(scorePose)))
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();



    }

    public void autonomousPathUpdate(){

        switch(pathState){

            case -1:
                telemetry.addLine("Auton has Finished! (Hopefully successfully)");
            case 0:
                follower.followPath(startToScore);
                setPathState(1);
                break;
            case 1:
                if(!follower.isBusy()){
                    //score
                    follower.followPath(scoreToPark);
                    setPathState(2);
                }
            case 2:
                //Set angle of lever to grab spec
                if(!follower.isBusy()){
                    //grab 2nd specimen
                    follower.followPath(parkToScore);
                    setPathState(3);
                }
            case 3:
                //Set angle of lever to score spec
                if(!follower.isBusy()){
                    //score
                    follower.followPath(scoreToPark);
                    setPathState(-1);
                }
        }
    }

    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    @Override
    public void init() {
        intake = new IntakeSubsystem(hardwareMap);
        outtake = new OuttakeSubsystem(hardwareMap);
        pathTimer = new Timer();
        Constants.setConstants(FConstants.class, LConstants.class);
        follower = new Follower(hardwareMap,FConstants.class,LConstants.class);
        follower.setStartingPose(startPose);
        buildPaths();
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    }

    @Override
    public void loop() {
        follower.update();
        autonomousPathUpdate();
        intake.update();
        outtake.update();
        telemetry.addData("Path State", pathState);
        telemetry.addData("Position", follower.getPose().toString());
        telemetry.update();
    }
}