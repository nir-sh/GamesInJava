package gameOBjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.time.LocalDateTime;
import java.util.function.Consumer;


public class BotPaddle extends GameObject {
    private static final float PADDLE_SPEED = 450;
    private static final float MIN_DISTANCE_FROM_SCREEN_EDGE = 14;
    public static final int PADDLE_LIFE_TIME = 10;
    private final GameObjectCollection gameObjects;
    private final Vector2 windowDimensions;
    private final LocalDateTime startTime;

    private Consumer<GameObject> movementMethod;


    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner    Position of the object, in window coordinates (pixels).
     *                         Note that (0,0) is the top-left corner of the window.
     * @param dimensions       Width and height in window coordinates.
     * @param renderable       The renderable representing the object. Can be null, in which case
     *                         the GameObject will not be rendered.
     * @param gameObjects
     * @param windowDimensions
     * @param goodBot
     */
    public BotPaddle(
            Vector2 topLeftCorner,
            Vector2 dimensions,
            Renderable renderable,
            GameObjectCollection gameObjects,
            Vector2 windowDimensions,
            boolean goodBot
            ) {
        super(topLeftCorner, dimensions, renderable);
        this.gameObjects = gameObjects;
        this.windowDimensions = windowDimensions;

        if (goodBot) {
            movementMethod = this::goodBotMovement;
        }
        else{
            movementMethod = this::badBotMovement;

        }

        this.startTime = LocalDateTime.now();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        //
        if(LocalDateTime.now().isAfter(startTime.plusSeconds(PADDLE_LIFE_TIME))){
            gameObjects.removeGameObject(this);
        }
// Ensure the paddle does not go beyond the left edge of the screen
        float minX = Math.max(MIN_DISTANCE_FROM_SCREEN_EDGE, getTopLeftCorner().x());
        float maxX = windowDimensions.x() - MIN_DISTANCE_FROM_SCREEN_EDGE - getDimensions().x();

        // Ensure the paddle does not go beyond the right edge of the screen
        float newX = Math.min(maxX, minX);

        setTopLeftCorner(new Vector2(newX, getTopLeftCorner().y()));


        GameObject closestBall = getClosestBall(this.getCenter(), gameObjects);
        if(closestBall == null) return;
        movementMethod.accept(closestBall);

    }

    private void goodBotMovement(GameObject closestBall) {
        if(closestBall.getCenter().y() < this.getCenter().y()){
            getCloserToBall(closestBall);
        }
        else{
            getAwayFromBall(closestBall);
        }
    }
    private void badBotMovement(GameObject closestBall) {
        if(closestBall.getCenter().y() < this.getCenter().y()){
            getAwayFromBall(closestBall);
        }
        else{
            getCloserToBall(closestBall);
        }
    }

    private void getAwayFromBall(GameObject closestBall) {
var movementDirection = getVectorForGettingCloser(closestBall).rotated(180);
        this.setVelocity(movementDirection.mult(PADDLE_SPEED));
    }


    private void getCloserToBall(GameObject closestBall) {
        var movementDirection = getVectorForGettingCloser(closestBall);
        this.setVelocity(movementDirection.mult(PADDLE_SPEED));
    }

    private Vector2 getVectorForGettingCloser(GameObject closestBall) {
        var ballCenter = closestBall.getCenter().x();
        var paddleCenter = this.getCenter().x();
        var movementDirection = Vector2.ZERO;
        if(paddleCenter < ballCenter-5){
            movementDirection = Vector2.RIGHT;
        }
        if (paddleCenter > ballCenter+5){
            movementDirection = Vector2.LEFT;
        }
        return movementDirection;
    }

    private GameObject getClosestBall(Vector2 paddlePosition, GameObjectCollection gameObjects){
        GameObject closestBall = null;
        var minDistance = distance(paddlePosition, new Vector2(7000, 7000));
        var iter = gameObjects.objectsInLayer(Layer.DEFAULT).iterator();
        while(iter.hasNext()){
            var current = iter.next();
            if(!(current instanceof Ball)){
                continue;
            }
            if(distance(paddlePosition, current.getCenter()) < minDistance){
                minDistance = distance(paddlePosition, current.getCenter());
                closestBall = current;
            }
        }
        return closestBall;

    }

    private double distance(Vector2 a, Vector2 b){
        var sub = a.subtract(b);
        return sub.magnitude();
    }
}
