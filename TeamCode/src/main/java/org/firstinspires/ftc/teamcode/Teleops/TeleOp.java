package org.firstinspires.ftc.teamcode.Teleops;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Subsystems.DriveSubsystem;
//HELLOOOOOOOOOOOO
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "Clanker")
public class
TeleOp extends OpMode {

    private DriveSubsystem myDriveTrain;


    private Gamepad currentGamepad  = new Gamepad();

    private Gamepad previousGamepad = new Gamepad();

    private static final double DEADBAND    = 0.1;

    private static final double DRIVE_SCALE = 0.5;

    @Override
    public void init() {
        myDriveTrain = new DriveSubsystem(hardwareMap);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void init_loop() {
        telemetry.update();
    }

    @Override
    public void loop() {
        previousGamepad.copy(currentGamepad);
        currentGamepad.copy(gamepad1);

        double y  = applyDeadband(-currentGamepad.left_stick_y) * DRIVE_SCALE;
        double x  = applyDeadband(currentGamepad.left_stick_x) * DRIVE_SCALE;
        double rx = applyDeadband(currentGamepad.right_stick_x) * DRIVE_SCALE;

        myDriveTrain.drive(y, x, rx);

        // --- Telemetry ---
        telemetry.addData("y / x / rx", "%.2f  %.2f  %.2f", y, x, rx);
        telemetry.addData("Heading (deg)", "%.1f", myDriveTrain.getHeadingDegrees());
        telemetry.update();
    }

    @Override
    public void stop() {
        myDriveTrain.stop();
    }

    private double applyDeadband(double value) {
        return Math.abs(value) < DEADBAND ? 0.0 : value;
    }
}
