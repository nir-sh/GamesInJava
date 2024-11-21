package bricker.brickStrategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.util.Counter;
import danogl.util.Vector2;
import gameOBjects.BallFactory;
import gameOBjects.Brick;
import gameOBjects.BotPaddle;
import gameOBjects.Heart;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BrickStrategyFactory {
    private static final int NUM_STRATEGIES = 1;
    public static final int MAX_AI_PADDLES = 2;
    private final GameObjectCollection gameObjects;
    private final Counter bricksCounter;
    Random random =new Random();
    private Counter ballsCounter;
    private final BallFactory ballFactory;
    private final Vector2 windowDimensions;
    private final ImageReader imageReader;
    private final Heart lives;

    public BrickStrategyFactory(GameObjectCollection gameObjects, Counter bricksCounter, Counter ballsCounter, BallFactory ballFactory, Vector2 windowDimensions, ImageReader imageReader, Heart lives){

        this.gameObjects = gameObjects;
        this.bricksCounter = bricksCounter;
        this.ballsCounter = ballsCounter;
        this.ballFactory = ballFactory;
        this.windowDimensions = windowDimensions;
        this.imageReader = imageReader;
        this.lives = lives;
    }

    public CollisionStrategy getStrategy(){
        Supplier<CollisionStrategy>[] strategySupplier = new Supplier[]{
            ()-> new RemoveBrickStrategy(gameObjects, bricksCounter),
            ()-> new AddBallStrategy(bricksCounter, gameObjects, ballsCounter, ballFactory),
            ()-> new AddStatusDefinerStrategy(
                imageReader.readImage("assets/buffWiden.png", true),
                gameObjects,
                windowDimensions,
                bricksCounter,
                expandPaddleAction()
            ),
            ()-> new AddStatusDefinerStrategy(
                imageReader.readImage("assets/buffNarrow.png", true),
                gameObjects,
                windowDimensions,
                bricksCounter,
                contractPaddleAction()
            ),
            ()-> new AddStatusDefinerStrategy(
                imageReader.readImage("assets/goodBot.png", true),
                gameObjects,
                windowDimensions,
                bricksCounter,
                goodBotAction()
            ),
            ()-> new AddStatusDefinerStrategy(
                    imageReader.readImage("assets/badBot.png", true),
                    gameObjects,
                    windowDimensions,
                    bricksCounter,
                    badBotAction()),
            ()-> new AddStatusDefinerStrategy(
                    imageReader.readImage("assets/heart.png", true),
                    gameObjects,
                    windowDimensions,
                    bricksCounter,
                    addLifeAction()),
            ()-> new MultiStrategiesStrategy(gameObjects, bricksCounter, getStrategy(), getStrategy() )
        };

        return strategySupplier[random.nextInt(strategySupplier.length)].get();
    }

    private Consumer<GameObject> addLifeAction() {
        return (GameObject otherObj)-> lives.setNumLives(lives.getMumLives() + 1);
    }

    private Consumer<GameObject> goodBotAction() {
        AtomicInteger botsCounter = new AtomicInteger();

        return (GameObject otherObj) ->{
            gameObjects.objectsInLayer(Layer.DEFAULT).forEach(
                    CountBots(botsCounter)
            );
            if (botsCounter.get() < MAX_AI_PADDLES) {
                var botPaddle = createBotPaddle(true, "assets/ovalPaddle.png");

                gameObjects.addGameObject(botPaddle);
            }
        };
    }

    private Consumer<GameObject> badBotAction() {
        AtomicInteger botsCounter = new AtomicInteger();

        return (GameObject otherObj) ->{
            gameObjects.objectsInLayer(Layer.DEFAULT).forEach(
                    CountBots(botsCounter)
            );
            if (botsCounter.get() < MAX_AI_PADDLES) {
                var botPaddle = createBotPaddle(false, "assets/badPaddle.png");

                gameObjects.addGameObject(botPaddle);
            }
        };
    }

    private BotPaddle createBotPaddle(boolean isGoodBot, String imagePath) {
        ArrayList<Brick> bricks = getAllBricks();
        var minBrick =
                bricks.stream().min((br1, br2) -> (int) (br1.getCenter().y() - br2.getCenter().y())).get();
        int highestPointForPaddle = (int) minBrick.getCenter().y();
        int lowestPointForPAddle = (int) (windowDimensions.y() - 50);
        var botPaddlePosition = random.nextInt(highestPointForPaddle, lowestPointForPAddle);
            return new BotPaddle(new Vector2(windowDimensions.x() / 2, botPaddlePosition),
                    getPaddleDimensions(),
                    imageReader.readImage(imagePath, true),
                    gameObjects,
                    windowDimensions,
                    isGoodBot);
    }

    private ArrayList<Brick> getAllBricks() {
        ArrayList<Brick> bricks = new ArrayList<>();
        gameObjects.objectsInLayer(Layer.STATIC_OBJECTS).forEach(
                obj -> {
                    if (obj instanceof Brick) {
                        bricks.add((Brick) obj);
                    }
                }
        );
        return bricks;
    }

    private static Consumer<GameObject> CountBots(AtomicInteger botsCounter) {
        return obj -> {
            if (obj instanceof BotPaddle) {
                botsCounter.addAndGet(1);
            }
        };
    }

    private Vector2 getPaddleDimensions() {
        return new Vector2(30, 15);
    }

    private static Consumer<GameObject> contractPaddleAction() {
        return (GameObject otherObj) -> {
            final float MIN_PADDLE_SIZE = 15;
            if (otherObj.getDimensions().x() > MIN_PADDLE_SIZE) {

                otherObj.setDimensions(
                        new Vector2(
                                otherObj.getDimensions().x() * 0.7f,
                                otherObj.getDimensions().y()
                        )
                );
            }
        };
    }

    private Consumer<GameObject> expandPaddleAction() {
        return (GameObject otherObj) -> {
            if (otherObj.getDimensions().x() < MaxPaddleSize()) {
                otherObj.setDimensions(
                        new Vector2(
                                otherObj.getDimensions().x() * 1.3f,
                                otherObj.getDimensions().y()
                        )
                );
            }
        };
    }

    private float MaxPaddleSize() {
        return windowDimensions.x() - 100;
    }
}
