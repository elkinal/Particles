package main;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by alxye on 02-Jul-19.
 */
public class Vector {
    private Point2D start;
    private Point2D end;

    public Vector(Point2D start, Point2D end) {
        this.start = start;
        this.end = end;
    }

    //Getting the Coordinates of the Vector
    public Point2D getStart() {
        return start;
    }

    public Point2D getEnd() {
        return end;
    }

    //Getting other important details of the vector
    public double getLength() {
        return Math.sqrt(Math.pow(start.getX()-end.getX(), 2) + Math.pow(start.getY()-end.getY(), 2));
    }

    public double getDX() {
        return end.getX()-start.getX();
    }

    public double getDY() {
        return end.getY()-start.getY();
    }

    public double getBearingRadians() {
        return Math.atan2(getDX(), getDY());
    }

    public double getBearingDegrees() {
        return Math.toDegrees(getBearingRadians()); // TODO: 02-Jul-19 FIX ERROR - This formula is wrong 
    }

    public Point2D getMidpoint() {
        return new Point2D(start.getX() + getDX()/2, start.getY() + getDY()/2);
    }

    public void drawLine(GraphicsContext graphics) {
        graphics.strokeLine(getStart().getX(), getStart().getY(), getEnd().getX(), getEnd().getY());
    }

    //Drawing
    public void draw(GraphicsContext graphics) {
        //Setting the color of the line
        graphics.setFill(Color.BLACK);

        //Drawing the main body of the line
        graphics.strokeLine(start.getX(), start.getY(), end.getX(), end.getY());

        //Drawing a point at the end of the line
        graphics.fillRect(end.getX()-2, end.getY()-2, 4, 4);

    }

}
