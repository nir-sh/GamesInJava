package bricker.brickStrategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

public class RemoveBrickStrategy implements CollisionStrategy {

    private final GameObjectCollection gameObjects;
    private final Counter bricksCounter;

    public RemoveBrickStrategy(GameObjectCollection gameObjects, Counter bricksCounter) {

        this.gameObjects = gameObjects;
        this.bricksCounter = bricksCounter;
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        if(gameObjects.removeGameObject(thisObj, Layer.STATIC_OBJECTS)){
            bricksCounter.decrement();
        }
    }
}
