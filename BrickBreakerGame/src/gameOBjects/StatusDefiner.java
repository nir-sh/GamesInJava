package gameOBjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.function.Consumer;

public class StatusDefiner extends GameObject {
    private final GameObjectCollection gameObjects;
    private final Vector2 windowDimensions;
    private final Consumer<GameObject> action;

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        double height = this.getCenter().y();
        if(height > windowDimensions.y()){

            gameObjects.removeGameObject(this);
        }
    }

    @Override
    public boolean shouldCollideWith(GameObject other) {
        if(other instanceof UserPaddle){
            return true;
        }
        return false;

    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {

        gameObjects.removeGameObject(this);
        action.accept(other);
    }



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
     * @param action
     */
    public StatusDefiner(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, GameObjectCollection gameObjects, Vector2 windowDimensions, Consumer<GameObject> action) {
        super(topLeftCorner, dimensions, renderable);
        this.gameObjects = gameObjects;
        this.windowDimensions = windowDimensions;
        this.action = action;
    }
}
