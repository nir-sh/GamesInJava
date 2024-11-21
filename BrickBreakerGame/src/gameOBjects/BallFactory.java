package gameOBjects;

import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class BallFactory {
    public static final int BALL_DIAMETER = 20;
    public static final int BALL_SPEED_DEFAULT = 250;

    private final ImageReader imageReader;
    private final SoundReader soundReader;
    private final ImageRenderable ballImage;
    private final Sound collisionSound;
    private final Vector2 windowDimensions;
    private final Counter ballCounter;
    private final GameObjectCollection gameObjects;

    public BallFactory(ImageReader imageReader, SoundReader soundReader, Vector2 windowDimensions, Counter ballCounter, GameObjectCollection gameObjects) {

        this.imageReader = imageReader;
        this.soundReader = soundReader;

        ballImage = imageReader.readImage("assets/ball.png", true);
        collisionSound = soundReader.readSound("assets/blop_cut_silenced.wav");
        this.windowDimensions = windowDimensions;
        this.ballCounter = ballCounter;
        this.gameObjects = gameObjects;
        collisionSound.stopAllOccurences();
    }

    public Ball createBall(){
        return new Ball(Vector2.ZERO, new Vector2(BALL_DIAMETER, BALL_DIAMETER), ballImage, collisionSound,
                windowDimensions, ballCounter, gameObjects);
    }
}
