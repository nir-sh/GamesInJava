package bricker.brickStrategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import gameOBjects.StatusDefiner;

import java.util.function.Consumer;

public class AddStatusDefinerStrategy extends RemoveBrickStrategy implements CollisionStrategy{
    private static final Vector2 DIMENSIONS = new Vector2(30, 30);
    private static final int FALL_SPEED = 200;
    private final Renderable renderable;
    private final GameObjectCollection gameObjects;
    private final Vector2 windowDimensions;
    private final Consumer<GameObject> action;

    public AddStatusDefinerStrategy(Renderable renderable, GameObjectCollection gameObjects, Vector2 windowDimensions
            , Counter bricksCounter
                                    , Consumer<GameObject> action){
        super(gameObjects, bricksCounter);
        this.renderable = renderable;
        this.gameObjects = gameObjects;
        this.windowDimensions = windowDimensions;
        this.action = action;
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        GameObject statusDefiner = new StatusDefiner(otherObj.getCenter(), DIMENSIONS, renderable, gameObjects,
                windowDimensions, action);
        statusDefiner.setVelocity(new Vector2(0, FALL_SPEED));

        gameObjects.addGameObject(statusDefiner);
    }
}
