package Controllers;

/**
 * Created by NCS Customer on 4/1/2015.
 */
public class PositionalOutputController extends OutputController {

    double[] localCenter;
    public PositionalOutputController(String controllerName, LeapValue leapValue, double minOutput, double maxOutput, int regressionSamples) {

        super(controllerName, leapValue, minOutput, maxOutput, regressionSamples);
    }

    public void updateLocalPositioning(double[] localCenter, double radius)
    {
        switch (leapValue)
        {
            case XPOSITION:
                minInput = localCenter[0] - radius;
                maxInput = localCenter[0] + radius;
                break;
            case YPOSITION:
                minInput = localCenter[1] - radius;
                maxInput = localCenter[1] + radius;
                break;
            case ZPOSITION:
                minInput = localCenter[2] - radius;
                maxInput = localCenter[2] + radius;
                flipSign = true;
                break;
        }
        generateRegression(samples);
    }
    public void updateLocalPositioning(double[] localCenter, double radius, double deadzonePadding)
    {
        radius = radius - deadzonePadding;
        switch (leapValue)
        {
            case XPOSITION:

                minInput = localCenter[0] - radius;
                maxInput = localCenter[0] + radius;
                break;
            case YPOSITION:
                minInput = localCenter[1] - radius;
                maxInput = localCenter[1] + radius;
                break;
            case ZPOSITION:
                minInput = localCenter[2] - radius;
                maxInput = localCenter[2] + radius;
                flipSign = true;
                break;
        }
        generateRegression(samples);
    }
}
