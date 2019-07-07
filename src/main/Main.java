package main;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.FileInputStream;
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
    public static double GRAV_CONSTANT = 0.001;
    public static final double DAMPENING = 0.00000;

    //Self-Explanatory
    public static boolean paused = false;

    //Images
    private static Image background;
    private static Image cursor;
    private static Image pauseScreen;

    //The size of the particle created when the user clicks on the screen
    public static int particleSize = 100;

    //All graphics are drawn using the GraphicsContext
    private GraphicsContext gc;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Detecting the screen width and height of the monitor to make the game more responsive
        SCREENHEIGHT = (int) Screen.getPrimary().getBounds().getHeight();
        SCREENWIDTH = (int) Screen.getPrimary().getBounds().getWidth();

        //Initializing the background image
        background = new Image(new FileInputStream("C:\\Users\\alxye\\IdeaProjects\\Particles\\src\\res\\background.png"));
        cursor = new Image(new FileInputStream("C:\\Users\\alxye\\IdeaProjects\\Particles\\src\\res\\cursor.png"));
        pauseScreen = new Image(new FileInputStream("C:\\Users\\alxye\\IdeaProjects\\Particles\\src\\res\\paused_screen.png"));

        //TESTING AREA
        /*particles.add(new Particle(300, Color.RED, new Point2D(500, 500)));
        particles.add(new Particle(100, Color.RED, new Point2D(800, 500)));
        particles.add(new Particle(100, Color.RED, new Point2D(1000, 500)));
        particles.add(new Particle(100, Color.BLUE, new Point2D(650, 500)));

        particles.get(0).setVelocity(new Point2D(1, 0));
        particles.get(1).setVelocity(new Point2D(-1, 0));
        particles.get(2).setVelocity(new Point2D(-1, 0));
        particles.get(3).setVelocity(new Point2D(-1, 0));*/

        //ORBITALS TEST
        /*particles.add(new Particle(1000, Color.RED, new Point2D(600, 500)));
        particles.add(new Particle(100, Color.RED, new Point2D(500, 500)));
        particles.get(1).accelerate(new Point2D(0, 1.75));*/

        //RANDOM PARTICLE TEST
        for (int i = 0; i < 10; i++) {
            particles.add(new Particle((int)rand(10,10), Color.BLUE, new Point2D(rand(0, 1920), rand(0, 1080))));
        }


        //Forces the game to be played full-screen
        primaryStage.setTitle("N-Body Simulation");
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        //Structural stuff for the window
        Group root = new Group();
        Scene scene = new Scene(root);

        //Changing the default cursor
        scene.setCursor(new ImageCursor(cursor));

        //Responding to the inputs of the user
        scene.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.SPACE)
                paused = !paused;
        });
        scene.setOnMouseClicked(event -> {
            particles.add(new Particle(particleSize, Color.BLACK, new Point2D(event.getX(), event.getY())));
        });

        //Allowing the user to change the size of the created particle using the scroll wheel
        scene.setOnScroll(event -> {
            if(event.getDeltaY() < 0)
                particleSize += 50;
            else
                particleSize -= (particleSize > 20) ? 20 : 0;
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

        //Drawing the Background
        graphics.drawImage(background, 0, 0, SCREENWIDTH, SCREENHEIGHT);

        //All graphics methods go here
        graphics.setFill(Color.BLACK);

        //Drawing all of the Particles
        particles.forEach(p -> p.draw(graphics));


        //Drawing the average FPS in the corner of the screen
        graphics.setFill(Color.GREEN);
        graphics.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        graphics.fillText("FPS: " + getFPS(), SCREENWIDTH-65, 12);
        graphics.fillText("Particles: " + particles.size(), SCREENWIDTH-100, 24);

        //Drawing the pause screen when the game is paused
        if(paused)
            graphics.drawImage(pauseScreen, 0, 0, SCREENWIDTH, SCREENHEIGHT);
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
        // TODO: 05-Jul-19 unexpected ArrayIndexOutOfBoundsException can rarely arise in the collision detection part
        for (int i = 0; i < particles.size(); i++) {
            for (int j = 0; j < particles.size(); j++) {
                if(particles.get(j).getLocation().distance(particles.get(i).getLocation())
                        < (particles.get(j).getDimensions()/2 + particles.get(i).getDimensions()/2)
                        && particles.get(i) != particles.get(j)) {
                    if(particles.get(j).getMass() > particles.get(i).getMass()) {
                        //Larger particle changes its trajectory according to Newton's Third Law
                        particles.get(j).setVelocity(
                                new Point2D(
                                        (particles.get(j).getMass() * particles.get(j).getVelocity().getX() +
                                                particles.get(i).getMass() * particles.get(i).getVelocity().getX()) /
                                                (particles.get(j).getMass() + particles.get(i).getMass()),

                                        (particles.get(j).getMass() * particles.get(j).getVelocity().getY() +
                                                particles.get(i).getMass() * particles.get(i).getVelocity().getY()) /
                                                (particles.get(j).getMass() + particles.get(i).getMass())
                                )
                        );
                        //Larger particle absorbs smaller particle
                        particles.get(j).addMass(particles.get(i).getMass());
                        particles.remove(particles.get(i));
                    }
                    else {

                        //Larger particle changes its trajectory according to Newton's Third Law
                        particles.get(i).setVelocity(
                                new Point2D(
                                        (particles.get(j).getMass() * particles.get(j).getVelocity().getX() +
                                                particles.get(i).getMass() * particles.get(i).getVelocity().getX()) /
                                                (particles.get(i).getMass() + particles.get(j).getMass()),

                                        (particles.get(j).getMass() * particles.get(j).getVelocity().getY() +
                                                particles.get(i).getMass() * particles.get(i).getVelocity().getY()) /
                                                (particles.get(i).getMass() + particles.get(j).getMass())
                                )
                        );
                        //Larger particle absorbs smaller particle
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
