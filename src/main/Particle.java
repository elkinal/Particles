package main;

/**
 * Created by alxye on 02-Jul-19.
 */
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import static java.lang.Math.*;

public class Particle {
    private int mass;
    private Color color;
    private Point2D movement;
    private Point2D location;
    private Point2D velocity;

    public Particle(int mass, Color color, Point2D location) {
        this.mass = mass;
        this.color = color;
        this.location = location;
        this.movement = new Point2D(100, 100);
        this.velocity = new Point2D(0, 0);
    }

    //Getting the diameter of the particle (assuming it is 3D) // TODO: 03-Jul-19 double-check if this is correct
    public double getDimensions() {
        return 2 * cbrt(mass * 3/4 * PI) * Main.SCALE;
    }

    public Point2D getCenterLocation() {
        return new Point2D(location.getX() - getDimensions()/2, location.getY() - getDimensions()/2);
    }

    //Getter and Setter for the location of the particle
    public Point2D getLocation() {
        return location;
    }

    public void setLocation(Point2D location) {
        this.location = location;
    }

    //Getter for the mass of the particle
    public int getMass() {
        return mass;
    }

    //Adder for the mass of the particle
    public void addMass(double mass) {
        this.mass += mass;
    }

    //Getter for the movement
    public Point2D getMovement() {
        return movement;
    }

    //Setter for the Movement Vector
    public void setMovement(Point2D movement) {
        this.movement = movement;
    }

    //This function changes the velocity off the particle
    public void accelerate(Point2D velocity) {
        this.velocity = this.velocity.add(velocity);
    }

    //Setter for the velocity
    public void setVelocity(Point2D velocity) {
        this.velocity = velocity;
    }

    //Getter for the velocity
    public Point2D getVelocity() {
        return this.velocity;
    }

    //This method contains all the calculations performed on the particle each frame
    public void tick() {
        //Moving the particle

        //EXPERIMENTAL - TERMINAL VELOCITY
        /*int t = 10;
        if(velocity.getX() > t)
            velocity = new Point2D(t, velocity.getY());
        if(velocity.getX() < -t)
            velocity = new Point2D(-t, velocity.getY());
        if(velocity.getY() > t)
            velocity = new Point2D(velocity.getX(), t);
        if(velocity.getY() < -t)
            velocity = new Point2D(velocity.getX(), -t);*/

        //Accelerating the particle
        location = location.add(velocity);


        //Experimental, displaying the velocity of each particle
//        System.out.println("Xvel: " + velocity.getX() + "Yvel: " + velocity.getY());
    }

    //This method draws the particle on the GraphicsContext each frame
    public void draw(GraphicsContext graphics) {
        graphics.setStroke(color);
        graphics.strokeOval(getCenterLocation().getX(), getCenterLocation().getY(), getDimensions(), getDimensions());

        //Drawing the movement vectors of each particle (TESTING ONLY - DELETE LATER)
//        drawVector(graphics);
    }

    //Drawing the vector itself, as a line (only for development purposes)
    public void drawVector(GraphicsContext graphics) {
        //Setting the color of the line
        graphics.setFill(Color.BLACK);

        //Drawing the main body of the line
        graphics.strokeLine(location.getX(), location.getY(), location.getX()+movement.getX(), location.getY()+movement.getY());

        //Drawing a point at the end of the line
        graphics.fillRect(location.getX()-2, location.getY()-2, 4, 4);

    }
}
