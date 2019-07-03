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
    private Vector movement;

    public Particle(int mass, Color color, Point2D location) {
        this.mass = mass;
        this.color = color;
        movement = new Vector(location);
    }

    //Getting the diameter of the particle (assuming it is 3D) // TODO: 03-Jul-19 double-check if this is correct
    public double getDimensions() {
        return 2 * cbrt(mass * 3/4 * PI) * Main.SCALE;
    }

    public Point2D getCenterLocation() {
        return new Point2D(movement.getStart().getX() - getDimensions()/2, movement.getStart().getY() - getDimensions()/2);
    }

    //This method contains all the calculations performed on the particle each frame
    public void tick() {

    }

    //This method draws the particle on the GraphicsContext each frame
    public void draw(GraphicsContext graphics) {
        graphics.setStroke(color);
        graphics.strokeOval(getCenterLocation().getX(), getCenterLocation().getY(), getDimensions(), getDimensions());

        //Drawing the movement vectors of each particle (TESTING ONLY - DELETE LATER)
        movement.draw(graphics);
    }
}
