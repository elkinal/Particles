package main;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;

public class Main extends Application {

    //Stores the height and width of the screen
    public static int SCREENWIDTH;
    public static int SCREENHEIGHT;

    //All items are given a size relative to the "scale"
    public static float SCALE = 1;

    //Stores the time since the last frame was loaded
    private static long deltaTime;
    private static long lastFrameTime = 0;

    //Stores the number of frames that have been computed
    public static long frames = 0;

    //This list stores all of particles in the entire simulation
    private ArrayList<Particle> particles = new ArrayList<>();

    //Gravitational Constant
    public static final double GRAV_CONSTANT = 0.01;
    public static final double DAMPENING = 0.00001;

    //All graphics are drawn using the GraphicsContext
    private GraphicsContext gc;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Detecting the screen width and height of the monitor to make the game more responsive
        SCREENHEIGHT = (int) Screen.getPrimary().getBounds().getHeight();
        SCREENWIDTH = (int) Screen.getPrimary().getBounds().getWidth();

        //TESTING AREA
//        particles.add(new Particle(100, Color.RED, new Point2D(1000, 1000)));
//        particles.add(new Particle(1000, Color.BLUE, new Point2D(500, 500)));
//        particles.add(new Particle(100, Color.VIOLET, new Point2D(300, 550)));

        //ORBITALS TEST
        /*particles.add(new Particle(1000, Color.RED, new Point2D(800, 500)));
        particles.add(new Particle(100, Color.RED, new Point2D(500, 500)));
        particles.get(1).accelerate(new Point2D(0, 1.4));*/

        //RANDOM PARTICLE TEST
        for (int i = 0; i < 30; i++) {
            particles.add(new Particle((int)rand(50,200), Color.RED, new Point2D(rand(0, 1920), rand(0, 1080))));
        }


        //Forces the game to be played full-screen
        primaryStage.setTitle("Elkin's cool game");
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        //Structural stuff for the window
        Group root = new Group();
        Scene scene = new Scene(root);

        //Responding to the inputs of the user
        scene.setOnKeyPressed(event -> {
            /*if(event.getCode() == KeyCode.Q)
                scale += 0.25;*/
        });
        scene.setOnMouseClicked(event -> {
            //event
        });

        scene.setOnScroll(event -> {
            //change scale
        });


        //Graphical operations
        primaryStage.setScene(scene);
        Canvas canvas = new Canvas(SCREENWIDTH, SCREENHEIGHT);
        root.getChildren().add(canvas);
        gc = canvas.getGraphicsContext2D();





        //This is the main game loop - everything is controlled from here
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                lastFrameTime = System.nanoTime(); //stores the time before each frame
                update(); //handles all of the calculations
                render(gc); //handles all of the graphical operations
                deltaTime = System.nanoTime() - lastFrameTime; //stores the time taken for each frame
            }
        }.start();
        primaryStage.show();
    }


    private void render(GraphicsContext graphics) { //synchronizing the block does not solve the error

        //Clearing the screen
        graphics.clearRect(0, 0, SCREENWIDTH, SCREENHEIGHT);

        //All graphics methods go here
        graphics.setFill(Color.BLACK);

        //Drawing all of the Particles
        particles.forEach(p -> p.draw(graphics));


        //Drawing the average FPS in the corner of the screen
        graphics.setFill(Color.GREEN);
        graphics.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        graphics.fillText("FPS: " + getFPS(), SCREENWIDTH-65, 12);

        //Resetting the screen
    }


    private double getFPS() {
        return Math.round(1/(deltaTime+Float.MIN_VALUE) * 10000000 * 10000.0)/1000.0;
    }


    private void update() {
       //All calculations go here
        //Physics Calculations - MOVEMENT
        for (int i = 0; i < particles.size(); i++) {
            for (int j = 0; j < particles.size(); j++) {
                //This calculates the total force between two particles. If the two particles are the same, the returned force is -1
                // TODO: 04-Jul-19 There seems to be an element of randomness in the way the particles behave. This should not be the case. This is almost certainly linked to the variation in the FPS
                //EXPERIMENTAL CODE ------------------------------------------------------------------------------------
                double force = (particles.get(i) != particles.get(j)) ?
                        (particles.get(i).getMass() * particles.get(j).getMass() / Math.pow(particles.get(j).getLocation().distance(particles.get(i).getLocation()), 2) + DAMPENING) : -1;

                double xDifference = particles.get(i).getLocation().getX() - particles.get(j).getLocation().getX();
                double yDifference = particles.get(i).getLocation().getY() - particles.get(j).getLocation().getY();
                double acceleration = force / particles.get(j).getMass();
                if(force > 0) {
                    particles.get(j).accelerate(new Point2D(xDifference * GRAV_CONSTANT * force * acceleration * deltaTime/1000000,
                            yDifference * GRAV_CONSTANT * force * acceleration * deltaTime/1000000));
                }


//                System.out.println("Force between " + j + " and " + i + " is: " + force);
            }
        }

        //Physics Calculations - COLLISION DETECTION
        for (int i = 0; i < particles.size(); i++) {
            for (int j = 0; j < particles.size(); j++) {
                if(particles.get(j).getLocation().distance(particles.get(i).getLocation())
                        < (particles.get(j).getDimensions()/2 + particles.get(i).getDimensions()/2)
                        && particles.get(i) != particles.get(j)) {
                    if(particles.get(j).getMass() > particles.get(i).getMass()) {
                        particles.get(j).addMass(particles.get(i).getMass());
                        particles.remove(particles.get(i));
                    }
                    else {
                        particles.get(i).addMass(particles.get(j).getMass());
                        particles.remove(particles.get(j));
                    }

                }
            }
        }

        //Ticking the Particles
        particles.forEach(p -> p.tick());

        //Incrementing the number of elapsed frames (for development purposes)
        frames++;
    }









    //Generates a random number between "max" and "min"
    public static float rand(float min, float max) {
        return new Random().nextInt((int) (max - min + 1)) + min;
    }

    //Launches the Main function
    public static void main(String[] args) {
        launch(args);
    }

}
