package bricker.brickStrategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

public class MultiStrategiesStrategy extends RemoveBrickStrategy{

    private final CollisionStrategy strategy1;
    private final CollisionStrategy strategy2;

    public MultiStrategiesStrategy(GameObjectCollection gameObjects, Counter bricksCounter, CollisionStrategy strategy1
            , CollisionStrategy strategy2) {
        super(gameObjects, bricksCounter);
        this.strategy1 = strategy1;
        this.strategy2 = strategy2;
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);

        strategy1.onCollision(thisObj, otherObj);
        strategy2.onCollision(thisObj, otherObj);
    }
}
