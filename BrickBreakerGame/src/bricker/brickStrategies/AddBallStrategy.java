package bricker.brickStrategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;
import danogl.util.Vector2;
import gameOBjects.Ball;
import gameOBjects.BallFactory;

public class AddBallStrategy extends RemoveBrickStrategy implements CollisionStrategy{
    private final GameObjectCollection gameObjects;
    private final Counter ballsCounter;
    private final BallFactory ballFactory;

    public AddBallStrategy(Counter brickCounter, GameObjectCollection gameObjects, Counter ballsCounter,
                           BallFactory ballFactory) {
        super(gameObjects, brickCounter);
        this.gameObjects = gameObjects;
        this.ballsCounter = ballsCounter;
        this.ballFactory = ballFactory;
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        var brickCenter = thisObj.getCenter();

        super.onCollision(thisObj, otherObj);

        Ball collidedBall = otherObj instanceof Ball ? (Ball) otherObj : null;
        if(collidedBall == null)
            return;
        var collidedBallVel = collidedBall.getVelocity();

        var ballDimensions = collidedBall.getDimensions();
        var newBall = ballFactory.createBall();
        newBall.setVelocity(new Vector2(collidedBallVel.x() * -1, collidedBallVel.y()));
        newBall.setCenter(brickCenter);

        gameObjects.addGameObject(newBall);
        ballsCounter.increment();
    }
}
