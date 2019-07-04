package main;

/**
 * Created by alxye on 04-Jul-19.
 */
public class Velocity {
    private double x;
    private double y;

    public Velocity(double x, double y) {
        this.x = x;
        this.y = y;
    }

    //Getters and setters for both the X and the Y
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void addX(double x) {
        this.x += x;
    }

    public void addY(double y) {
        this.y += y;
    }
}
