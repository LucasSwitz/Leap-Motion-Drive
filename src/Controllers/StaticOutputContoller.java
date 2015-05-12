package Controllers;

/**
 * Created by NCS Customer on 4/1/2015.
 */

public class StaticOutputContoller extends OutputController {

    public StaticOutputContoller(String controllerName, LeapValue leapValue, double minInput, double maxInput, double minOutput, double maxOutput, int regressionSamples) {
        super(controllerName, leapValue, minInput, maxInput, minOutput, maxOutput, regressionSamples);
    }
}
