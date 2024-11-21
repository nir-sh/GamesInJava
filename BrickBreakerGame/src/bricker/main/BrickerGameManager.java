package bricker.main;

import bricker.brickStrategies.BrickStrategyFactory;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import gameOBjects.*;

import java.awt.*;
import java.util.Random;


public class BrickerGameManager extends GameManager {

    private static final float BOARDER_WIDTH = 10f;
    private static final Color BORDER_COLOR = Color.CYAN;
    public static final int BALL_SPEED = 150;
            ;
    public static final int WINDOW_WIDTH = 700;
    public static final int WINDOW_HEIGHT = 500;
    public static final int PADDLE_WIDTH = 100;
    public static final int PADDLE_HEIGHT = 15;
    public static final int FIXED_BRICK_HEIGHT = 15;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private WindowController windowController;
    private Ball ball;
    private Vector2 windowDimensions;
    private Heart lives;
    private danogl.util.Counter bricksCounter = new Counter(0);
    private BallFactory ballFactory;
    private Counter ballCounter = new Counter(0);

    public BrickerGameManager(String windowTitle, Vector2 windowDimensions){
        super(windowTitle, windowDimensions);
    }
    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.windowController = windowController;
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        windowDimensions = windowController.getWindowDimensions();

        bricksCounter = new Counter(0);
        ballCounter = new Counter(0);
        ballFactory = new BallFactory(imageReader, soundReader, windowDimensions, ballCounter, gameObjects());
        createLives();

        //create objects
        createBall();
        createPaddle(inputListener);
        createBoarders();
        createBackground();
        createBricks();
    }

    private void createLives() {
        lives = new Heart(
                    new Vector2(20,20),
                    new Vector2(30, 30),
                    imageReader.readImage("assets/heart.png", true),
                    3);
        gameObjects().addGameObject(lives, Layer.FOREGROUND);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        String prompt = "";

        if(lives.getMumLives() == 0) {
            prompt += "you lose! :-0";
        }

        if(ballCounter.value() <= 0){
            lives.setNumLives(lives.getMumLives() - 1);
            createBall();
        }

        if(!anyBrickLeft()){
            prompt += "you win! :-)";
        }

        if(!prompt.isEmpty()){
            if(windowController.openYesNoDialog(prompt + "\nplay another game?")){
                windowController.resetGame();
            }
            else{
                windowController.closeWindow();
            }
        }

    }

    private boolean anyBrickLeft() {
        if(bricksCounter.value() > 0){
            return true;
        }
        return false;
    }

    private void createBricks() {

        var brickImage = imageReader.readImage("assets/brick.png", false);
        var bricksFactory = new BrickStrategyFactory(
                gameObjects(),
                bricksCounter,
                ballCounter,
                ballFactory,
                windowDimensions,
                imageReader,
                lives);
        // Define constants for spacing between bricks and borders
        final int BRICK_SPACING = 2; // Between bricks
        final int BORDER_SPACING = 60; // Between bricks and window borders

        // Calculate the number of rows and columns
        int numRows = 3;
        int numCols = 8;

        // Calculate the width of each brick dynamically based on window dimensions
        int brickWidth = (int) ((windowDimensions.x() - 2 * BORDER_SPACING - (numCols - 1) * BRICK_SPACING) / numCols);

        // Loop to create multiple bricks
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                // Calculate position for each brick
                float x = BORDER_SPACING + col * (brickWidth + BRICK_SPACING);
                float y = BORDER_SPACING + row * (FIXED_BRICK_HEIGHT + BRICK_SPACING); // 15 is the fixed height

                // Create brick
                GameObject brick = new Brick(new Vector2(x, y), new Vector2(brickWidth, 15), brickImage,
                        bricksFactory.getStrategy());
                bricksCounter.increment();

                // Add brick to game objects
                gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
            }
        }

    }

    private void createBackground() {
        GameObject background = new GameObject(
                Vector2.ZERO,
                windowDimensions,
                imageReader.readImage("assets/DARK_BG2_small.jpeg",false));
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background, Layer.BACKGROUND);
    }

    private void createBoarders() {
        GameObject upperWall = new GameObject(Vector2.ZERO, new Vector2(windowDimensions.x(), BOARDER_WIDTH), new RectangleRenderable(BORDER_COLOR) );
        gameObjects().addGameObject(upperWall);
        GameObject leftWall = new GameObject(Vector2.ZERO, new Vector2(BOARDER_WIDTH, windowDimensions.y()), new RectangleRenderable(BORDER_COLOR) );
        gameObjects().addGameObject(leftWall);
        GameObject rightWall = new GameObject(new Vector2(windowDimensions.x() - BOARDER_WIDTH, 0.0f), new Vector2(BOARDER_WIDTH, windowDimensions.y()), new RectangleRenderable(BORDER_COLOR) );
        gameObjects().addGameObject(rightWall);
    }

    private void createPaddle(UserInputListener inputListener) {
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
        GameObject paddle = new UserPaddle(Vector2.ZERO,
                new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT),
                paddleImage,
                inputListener,
                windowDimensions);
        paddle.setCenter(new Vector2(windowDimensions.x()/2, windowDimensions.y() - 30));

        this.gameObjects().addGameObject(paddle);
    }

    private void createBall() {
        ball = ballFactory.createBall();

        Vector2 ballVector = getRandomBallVector();
        ball.setVelocity(new Vector2(ballVector.x() * BALL_SPEED, ballVector.y() * BALL_SPEED));
        setBallPositionToCenter();

        this.gameObjects().addGameObject(ball);
        ballCounter.increment();
    }

    private void setBallPositionToCenter() {
        ball.setCenter(windowDimensions.mult(0.4F));
    }

    private static Vector2 getRandomBallVector() {
        Random random = new Random();
        float xVel = 1;
        float yVel = 1;
        if(random.nextBoolean()){
            xVel *= -1;
        }
        if(random.nextBoolean()){
            yVel *= -1;
        }
        return new Vector2(xVel,yVel);
    }

    public static void main(String[] args){
        new BrickerGameManager("Bricker", new Vector2(WINDOW_WIDTH, WINDOW_HEIGHT)).run();
    }
}
