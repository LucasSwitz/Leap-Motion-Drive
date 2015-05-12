package Controllers;

/**
 * Created by NCS Customer on 4/1/2015.
 */
public abstract class OutputController {

    double minInput, maxInput, minOutput, maxOutput;
    String controllerName;
    String valueName;
    LeapValue leapValue;
    int samples;
    double a,b;

    public enum LeapValue{
        XPOSITION,
        YPOSITION,
        ZPOSITION,
        PALMROLL,
        FINGERCOUNT
    }

    boolean flipSign;

    public OutputController(String controllerName, LeapValue leapValue, double minInput, double maxInput, double minOutput, double maxOutput, int regressionSamples)
    {
        this.minInput = minInput;
        this.maxInput = maxInput;
        this.minOutput = minOutput;
        this.maxOutput = maxOutput;
        this.controllerName = controllerName;
        this.leapValue = leapValue;
        samples = regressionSamples;

        flipSign = false;

        switch (leapValue) {
            case XPOSITION:
                valueName = "X Position";
                break;
            case YPOSITION:
                valueName ="Y Position";
                break;
            case ZPOSITION:
                valueName ="Z Position";
                break;
            case PALMROLL:
                valueName ="Palm Roll";
                break;
            case FINGERCOUNT:
                valueName ="Fingers Extended";
                break;
        }

        generateRegression(regressionSamples);
    }

    public OutputController(String controllerName, LeapValue leapValue,double minOutput, double maxOutput, int regressionSamples)
    {
        this.minOutput = minOutput;
        this.maxOutput = maxOutput;
        this.controllerName = controllerName;
        this.leapValue = leapValue;
        samples = regressionSamples;

        switch (leapValue) {
            case XPOSITION:
                valueName = "X Position";
                break;
            case YPOSITION:
                valueName ="Y Position";
                break;
            case ZPOSITION:
                valueName ="Z Position";
                break;
            case PALMROLL:
                valueName ="Palm Roll";
                break;
        }
    }
    protected void generateRegression(int samples)
    {
        double[][] regressionChart = new double[samples+1][5];
        double inputRange = maxInput-minInput;
        double outputRange = maxOutput - minOutput;

        for(int i = 0; i < samples; i++)
        {
            double percentile = (double)i/(samples-1);

            regressionChart[i][0] = minInput + inputRange*percentile;
            regressionChart[i][1] = minOutput + outputRange*percentile;
            regressionChart[i][2] = regressionChart[i][0] * regressionChart[i][1];
            regressionChart[i][3] = Math.pow(regressionChart[i][0],2);
        }

        for(int i = 0; i <= 3; i++)
        {
            double sum = 0;
            for(int k = 0; k <= samples; k++)
            {
                sum += regressionChart[k][i];
            }
            regressionChart[samples][i] = sum;
        }

        double sumX = regressionChart[samples][0];
        double sumY = regressionChart [samples][1];
        double sumXY = regressionChart[samples][2];
        double sumX2 = regressionChart[samples][3];

         a = ((sumY * sumX2) - (sumX * sumXY)) / ((samples * sumX2) - Math.pow(sumX,2));
         b = ((samples * sumXY) - (sumX * sumY)) / ((samples * sumX2) - Math.pow(sumX,2));

    }


    public ControllerOutput getOutput(double input)
    {
        double output = a + b*input;

        if(flipSign) output = -output;
        if(output > maxOutput) output = maxOutput;
        if(output < minOutput) output = minOutput;

        return new ControllerOutput(this.controllerName,output);
    }

    public String getLeapValue()
    {
        return valueName;
    }

    public void setInputs(double min, double max)
    {
        minInput = min;
        maxInput = max;
        generateRegression(samples);
    }

    public String getOutputValueName()
    {
        return controllerName;
    }

   public class ControllerOutput
    {
        public String name;
        public Double value;
        ControllerOutput(String name, Double value)
        {
            this.name = name;
            this.value = value;
        }
    }

}

