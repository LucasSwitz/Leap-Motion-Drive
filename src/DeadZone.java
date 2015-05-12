/**
 * Created by NCS Customer on 3/31/2015.
 */
public class DeadZone
{

    public  double radius;
    public double[] center = {0,0,0};

    public DeadZone(double radius)
    {
        this.radius = radius;
    }
    public DeadZone(double radius, double[] center)
    {
        this.radius = radius;
        this.center = center;
    }

    public boolean contains(double x, double y, double z) {

        if (Math.pow(x - center[0], 2.0) + Math.pow(y - center[1], 2.0) + Math.pow(z - center[2], 2.0) < Math.pow(radius, 2.0)) {
            return true;
        } else {
            return false;
        }

    }
    public void setCenter(double[] center)
    {
        this.center = center;
    }

    public double getRadius(){return radius;}
}
