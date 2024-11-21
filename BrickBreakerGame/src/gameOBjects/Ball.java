package gameOBjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class Ball extends GameObject {
    private final Sound collisionSound;
    private final Vector2 windowDimensions;
    private final Counter ballsCounter;
    private final GameObjectCollection gameObjects;


    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner    Position of the object, in window coordinates (pixels).
     *                         Note that (0,0) is the top-left corner of the window.
     * @param dimensions       Width and height in window coordinates.
     * @param renderable       The renderable representing the object. Can be null, in which case
     *                         the GameObject will not be rendered.
     * @param windowDimensions
     * @param ballCounter
     * @param gameObjects
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound, Vector2 windowDimensions, Counter ballCounter, GameObjectCollection gameObjects) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
        this.windowDimensions = windowDimensions;
        this.ballsCounter = ballCounter;
        this.gameObjects = gameObjects;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        Vector2 newVel = getVelocity().flipped(collision.getNormal());
        setVelocity(newVel);
        collisionSound.play();
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        setVelocity(getVelocity().mult(1.0001f));

        double ballHeight = this.getCenter().y();
        if(ballHeight > windowDimensions.y()){
            ballsCounter.decrement();

            gameObjects.removeGameObject(this);
        }
    }
}
