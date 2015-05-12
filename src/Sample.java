/******************************************************************************\
* Copyright (C) 2012-2013 Leap Motion, Inc. All rights reserved.               *
* Leap Motion proprietary and confidential. Not for distribution.              *
* Use subject to the terms of the Leap Motion SDK Agreement available at       *
* https://developer.leapmotion.com/sdk_agreement, or another agreement         *
* between Leap Motion and you, your company or other organization.             *
\******************************************************************************/

import java.io.IOException;
import java.lang.Math;
import java.util.HashMap;

import Controllers.OutputController;
import Controllers.PositionalOutputController;
import Controllers.StaticOutputContoller;
import com.leapmotion.leap.*;
import com.leapmotion.leap.Gesture.State;

class SHARPLeapMotionListener extends Listener {

    HashMap<String,Double> valueMap;

    public void setValueMap(HashMap<String, Double> valueMap)
    {
        this.valueMap = valueMap;
    }
    public void onInit(Controller controller) {
        System.out.println("Initialized");
    }

    public void onConnect(Controller controller) {
        System.out.println("Connected");
        controller.enableGesture(Gesture.Type.TYPE_SWIPE);
        controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
        controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
        controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
    }

    public void onDisconnect(Controller controller) {
        //Note: not dispatched when running in a debugger.
        System.out.println("Disconnected");
    }

    public void onExit(Controller controller) {
        System.out.println("Exited");
    }

    public void onFrame(Controller controller) {
        // Get the most recent frame and report some basic information
        int fingeresExtended = 0;
        Hand focusedHand = null;
        Frame frame = controller.frame();

            for(Hand hand : frame.hands())
            {
                if(hand.isLeft() && valueMap.get("Focused Hand") == 0.0)
                {
                    focusedHand = hand;
                }
                else if(hand.isRight() && valueMap.get("Focused Hand") == 1.0)
                {
                    focusedHand = hand;
                }
            }


        for(Finger finger : focusedHand.fingers())
        {
            if(finger.isExtended())
            {
                fingeresExtended++;
            }
        }
        valueMap.put("Fingers Extended", (double)fingeresExtended);
        valueMap.put("X Position", (double) focusedHand.palmPosition().getX());
        valueMap.put("Y Position",(double)focusedHand.palmPosition().getY());
        valueMap.put("Z Position",(double)focusedHand.palmPosition().getZ());

        valueMap.put("Palm Roll",(double)focusedHand.palmNormal().roll());

        //System.out.println(frame.hands().count());

        /*System.out.println("Frame id: " + frame.id()
                         + ", timestamp: " + frame.timestamp()
                         + ", hands: " + frame.hands().count()
                         + ", fingers: " + frame.fingers().count()
                         + ", tools: " + frame.tools().count()
                         + ", gestures " + frame.gestures().count());*/

        //Get hands
        /*for(Hand hand : frame.hands()) {
            String handType = hand.isLeft() ? "Left hand" : "Right hand";
            System.out.println("  " + handType + ", id: " + hand.id()
                             + ", palm position: " + hand.palmPosition());

            // Get the hand's normal vector and direction
            Vector normal = hand.palmNormal();
            Vector direction = hand.direction();

            // Calculate the hand's pitch, roll, and yaw angles
            System.out.println("  pitch: " + Math.toDegrees(direction.pitch()) + " degrees, "
                             + "roll: " + Math.toDegrees(normal.roll()) + " degrees, "
                             + "yaw: " + Math.toDegrees(direction.yaw()) + " degrees");

            // Get arm bone
            Arm arm = hand.arm();
            System.out.println("  Arm direction: " + arm.direction()
                             + ", wrist position: " + arm.wristPosition()
                             + ", elbow position: " + arm.elbowPosition());

            // Get fingers
            for (Finger finger : hand.fingers()) {
                System.out.println("    " + finger.type() + ", id: " + finger.id()
                                 + ", length: " + finger.length()
                                 + "mm, width: " + finger.width() + "mm");

                //Get Bones
                for(Bone.Type boneType : Bone.Type.values()) {
                    Bone bone = finger.bone(boneType);
                    System.out.println("      " + bone.type()
                                     + " bone, start: " + bone.prevJoint()
                                     + ", end: " + bone.nextJoint()
                                     + ", direction: " + bone.direction());
                }
            }
        }

        // Get tools
        for(Tool tool : frame.tools()) {
            System.out.println("  Tool id: " + tool.id()
                             + ", position: " + tool.tipPosition()
                             + ", direction: " + tool.direction());
        }

        GestureList gestures = frame.gestures();
        for (int i = 0; i < gestures.count(); i++) {
            Gesture gesture = gestures.get(i);

            switch (gesture.type()) {
                case TYPE_CIRCLE:
                    CircleGesture circle = new CircleGesture(gesture);

                    // Calculate clock direction using the angle between circle normal and pointable
                    String clockwiseness;
                    if (circle.pointable().direction().angleTo(circle.normal()) <= Math.PI/2) {
                        // Clockwise if angle is less than 90 degrees
                        clockwiseness = "clockwise";
                    } else {
                        clockwiseness = "counterclockwise";
                    }

                    // Calculate angle swept since last frame
                    double sweptAngle = 0;
                    if (circle.state() != State.STATE_START) {
                        CircleGesture previousUpdate = new CircleGesture(controller.frame(1).gesture(circle.id()));
                        sweptAngle = (circle.progress() - previousUpdate.progress()) * 2 * Math.PI;
                    }

                    System.out.println("  Circle id: " + circle.id()
                               + ", " + circle.state()
                               + ", progress: " + circle.progress()
                               + ", radius: " + circle.radius()
                               + ", angle: " + Math.toDegrees(sweptAngle)
                               + ", " + clockwiseness);
                    break;
                case TYPE_SWIPE:
                    SwipeGesture swipe = new SwipeGesture(gesture);
                    System.out.println("  Swipe id: " + swipe.id()
                               + ", " + swipe.state()
                               + ", position: " + swipe.position()
                               + ", direction: " + swipe.direction()
                               + ", speed: " + swipe.speed());
                    break;
                case TYPE_SCREEN_TAP:
                    ScreenTapGesture screenTap = new ScreenTapGesture(gesture);
                    System.out.println("  Screen Tap id: " + screenTap.id()
                               + ", " + screenTap.state()
                               + ", position: " + screenTap.position()
                               + ", direction: " + screenTap.direction());
                    break;
                case TYPE_KEY_TAP:
                    KeyTapGesture keyTap = new KeyTapGesture(gesture);
                    System.out.println("  Key Tap id: " + keyTap.id()
                               + ", " + keyTap.state()
                               + ", position: " + keyTap.position()
                               + ", direction: " + keyTap.direction());
                    break;
                default:
                    System.out.println("Unknown gesture type.");
                    break;
            }
        }

        if (!frame.hands().isEmpty() || !gestures.isEmpty()) {
            System.out.println();
        }
    }*/
    }
}

class ControllerUpdater implements  Runnable {
    private SHARPLeapMotionDrive controller;
    private LeapMotionDash dashboard;

    public ControllerUpdater(SHARPLeapMotionDrive controller, LeapMotionDash dashboard) {
        this.controller = controller;
        this.dashboard = dashboard;
    }

    @Override
    public void run() {

        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            controller.update();
            dashboard.update();
        }
    }
}

    class Sample {
        public static void main(String[] args) {

            SHARPLeapMotionListener listener = new SHARPLeapMotionListener();
            SHARPLeapMotionDrive controller = new SHARPLeapMotionDrive();

            LeapMotionDash dashboard = new LeapMotionDash();
            dashboard.setController(controller);
            ControllerUpdater controllerUpdater = new ControllerUpdater(controller,dashboard);

            controller.addPositionalOutputContoller(new PositionalOutputController("X Output", OutputController.LeapValue.XPOSITION, -1, 1, 5));
            controller.addPositionalOutputContoller(new PositionalOutputController("Y Output",OutputController.LeapValue.YPOSITION,-1,1,5));
            controller.addPositionalOutputContoller(new PositionalOutputController("Z Output",OutputController.LeapValue.ZPOSITION,-1,1,5));

            controller.addStaticOutputController(new StaticOutputContoller("Fingers Extended", OutputController.LeapValue.FINGERCOUNT,0,5,0,5,5));


            controller.addStaticOutputController(new StaticOutputContoller("Rotate Output", OutputController.LeapValue.PALMROLL,-1.8,1.8,-1,1,5));
            controller.setHand(SHARPLeapMotionDrive.controllingHand.RIGHT_HAND);
            controller.addListener(listener);


            System.out.println("Press Enter to quit...");
            controllerUpdater.run();

            try {
                System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }

            controller.removeListener(listener);
        }
    }

