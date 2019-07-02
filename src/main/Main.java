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
import java.util.Random;

public class Main extends Application {

    //Stores the height and width of the screen
    public static int SCREENWIDTH;
    public static int SCREENHEIGHT;

    //All items are given a size relative to the "scale"
    public static float SCALE = 1;

    //Stores the time since the last frame was loaded
    public static long deltaTime;
    public static long lastFrameTime = 0;

    //Stores the number of frames that have been computed
    public static long frames = 0;

    public static Vector vector = new Vector(new Point2D(500, 500), new Point2D(600, 300));

    //All graphics are drawn using the GraphicsContext
    private GraphicsContext gc;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Detecting the screen width and height of the monitor to make the game more responsive
        SCREENHEIGHT = (int) Screen.getPrimary().getBounds().getHeight();
        SCREENWIDTH = (int) Screen.getPrimary().getBounds().getWidth();


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
//        for (int i = 0; i < 1000; i++) {
//            graphics.fillRect(0, 0, rand(0, 1000), rand(0, 1000));
//        }

        //Drawing a vector - TESTING
        vector.draw(graphics);
        System.out.println(vector.getBearingDegrees());



        //Drawing the average FPS in the corner of the screen
        graphics.setFill(Color.GREEN);
        graphics.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        graphics.fillText("FPS: " + getFPS(), SCREENWIDTH-65, 12);

        //Resetting the screen
    }


    private double getFPS() {
        return Math.round(1/(deltaTime+Float.MIN_VALUE)*10000000 * 10000.0)/1000.0;
    }


    private void update() {
       //All calculations go here

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
