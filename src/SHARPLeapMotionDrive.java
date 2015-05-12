import Controllers.OutputController;
import Controllers.PositionalOutputController;
import Controllers.StaticOutputContoller;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Interface;
import com.leapmotion.leap.Listener;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import javafx.geometry.Pos;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by NCS Customer on 3/30/2015.
 */
public class SHARPLeapMotionDrive extends Controller{

    //add Networkd table for position and fingers extended

    public NetworkTable table;

    public enum controllingHand
    {
        LEFT_HAND,
        RIGHT_HAND
    }

    private DeadZone lowerDeadZone, upperDeadZone;

    private double sensitivity;

    private controllingHand focusedHand;

    public HashMap<String,Double> valueMap;

    ArrayList<OutputController> outputControllers;
    ArrayList<StaticOutputContoller> staticOutputControllers;
    ArrayList<PositionalOutputController> positionalOutputControllers;

    private double xPos,yPos,zPos;

    public SHARPLeapMotionDrive()

    {
        NetworkTable.setClientMode();
        NetworkTable.setIPAddress("localhost");
        table = NetworkTable.getTable("Leap Variables");

        outputControllers = new ArrayList<OutputController>();
        positionalOutputControllers = new ArrayList<PositionalOutputController>();
        staticOutputControllers = new ArrayList<StaticOutputContoller>();
        valueMap = new HashMap<String, Double>();

        xPos = yPos = zPos = 0;

        lowerDeadZone = new DeadZone(20);
        upperDeadZone = new DeadZone(150);

        valueMap.put("X Position", 0.0);
        valueMap.put("Y Position", 0.0);
        valueMap.put("Z Position", 0.0);

        valueMap.put("X Output", 0.0);
        valueMap.put("Y Output", 0.0);
        valueMap.put("Z Output", 0.0);

        valueMap.put("Fingers Extended",0.0);

        valueMap.put("Palm Roll",0.0);
        valueMap.put("Rotate Output",0.0);
    }

    public void addListener(SHARPLeapMotionListener listener)
    {
        super.addListener(listener);
        listener.setValueMap(valueMap);
    }
    public void setControllerDeadZone(double lowerRadius, double upperRadius)
    {
        lowerDeadZone = new DeadZone(lowerRadius);
        upperDeadZone = new DeadZone(upperRadius);
    }
    public void setHand(controllingHand hand)
    {
       focusedHand = hand;

        if(focusedHand == controllingHand.LEFT_HAND)
        {
            valueMap.put("Focused Hand",0.0);

        }else{

            valueMap.put("Focused Hand",1.0);
        }
    }
    public void update()
    {
        if(!valueMap.isEmpty()) {
            xPos = valueMap.get("X Position");
            yPos = valueMap.get("Y Position");
            zPos = valueMap.get("Z Position");

            if((!lowerDeadZone.contains(xPos,yPos,zPos))  && upperDeadZone.contains(xPos,yPos,zPos))
            {
                for(OutputController currentController : outputControllers)
                {
                    double input = valueMap.get(currentController.getLeapValue());
                    OutputController.ControllerOutput output = currentController.getOutput(input);
                    table.putNumber(output.name,output.value);
                    valueMap.put(output.name, output.value);
                }
            }else
            {
                for(OutputController currentController : outputControllers)
                {
                    table.putNumber(currentController.getOutputValueName(),0);
                }
            }
        }
    }

    public void setSensitivity(double sensitivity) {
        this.sensitivity = sensitivity;
    }

    public void addStaticOutputController(StaticOutputContoller controller)
    {
        outputControllers.add(controller);
        staticOutputControllers.add(controller);
        table.putNumber(controller.getOutputValueName(),0);
    }
    public void addPositionalOutputContoller(PositionalOutputController controller)
    {
        outputControllers.add(controller);
        positionalOutputControllers.add(controller);
        table.putNumber(controller.getOutputValueName(),0);
    }

    public void setOrigin() {
        System.out.println("Setting center");

        double[] center = {xPos,yPos,zPos};
        lowerDeadZone.setCenter(center);
        upperDeadZone.setCenter(center);

        for(PositionalOutputController controller : positionalOutputControllers)
        {
            controller.updateLocalPositioning(center, upperDeadZone.getRadius(),50);
        }
    }

}
