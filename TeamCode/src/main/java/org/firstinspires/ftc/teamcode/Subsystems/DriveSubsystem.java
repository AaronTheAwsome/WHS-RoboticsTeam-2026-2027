package org.firstinspires.ftc.teamcode.Subsystems;

import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
public class DriveSubsystem {

    private static final String PINPOINT_HW_NAME = "pinpoint";

    private static final double X_POD_OFFSET_MM = -84.0;

    private static final double Y_POD_OFFSET_MM = -168.0;

    private static final GoBildaPinpointDriver.GoBildaOdometryPods POD_TYPE =
            GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD;

    private static final GoBildaPinpointDriver.EncoderDirection X_DIR =
            GoBildaPinpointDriver.EncoderDirection.FORWARD;

    private static final GoBildaPinpointDriver.EncoderDirection Y_DIR =
            GoBildaPinpointDriver.EncoderDirection.FORWARD;

    private static final double STRAFE_CORRECTION = 1.1;

    private final DcMotor frontLeft;
    private final DcMotor backLeft;
    private final DcMotor frontRight;
    private final DcMotor backRight;

    private final GoBildaPinpointDriver pinpoint;

    public DriveSubsystem(HardwareMap hardwareMap) {
        frontLeft  = hardwareMap.dcMotor.get(Constants.driveConstants.leftFrontMotorName);
        backLeft   = hardwareMap.dcMotor.get(Constants.driveConstants.leftRearMotorName);
        frontRight = hardwareMap.dcMotor.get(Constants.driveConstants.rightFrontMotorName);
        backRight  = hardwareMap.dcMotor.get(Constants.driveConstants.rightRearMotorName);

        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, PINPOINT_HW_NAME);

        pinpoint.setOffsets(X_POD_OFFSET_MM, Y_POD_OFFSET_MM);
        pinpoint.setEncoderResolution(POD_TYPE);
        pinpoint.setEncoderDirections(X_DIR, Y_DIR);

        pinpoint.resetPosAndIMU();
    }

    public void drive(double y, double x, double rx) {

        pinpoint.update();

        double botHeading = pinpoint.getHeading();

        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

        rotX *= STRAFE_CORRECTION;

        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);

        double frontLeftPower  = (rotY + rotX + rx) / denominator;
        double backLeftPower   = (rotY - rotX + rx) / denominator;
        double frontRightPower = (rotY - rotX - rx) / denominator;
        double backRightPower  = (rotY + rotX - rx) / denominator;

        frontLeft.setPower(frontLeftPower);
        backLeft.setPower(backLeftPower);
        frontRight.setPower(frontRightPower);
        backRight.setPower(backRightPower);
    }


    public void stop() {
        frontLeft.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(0);
    }

    public void resetYaw() {
        pinpoint.resetPosAndIMU();
    }

    public double getHeadingRadians() {
        return pinpoint.getHeading();
    }

    public double getHeadingDegrees() {
        return Math.toDegrees(pinpoint.getHeading());
    }

    public GoBildaPinpointDriver getPinpoint() {
        return pinpoint;
    }
}
