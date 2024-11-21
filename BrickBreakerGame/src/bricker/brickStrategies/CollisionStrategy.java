package bricker.brickStrategies;

import danogl.GameObject;

public interface CollisionStrategy {
    public void onCollision(GameObject thisObj, GameObject otherObj);
}
