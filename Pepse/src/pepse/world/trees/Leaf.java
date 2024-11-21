package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.Random;

public class Leaf extends Block {
    private static final float FADEOUT_TIME = 8;
    public static final int FALL_VELOCITY = 120;
    public static final int TIME_UNTIL_FALL_UPPER_BOUND = 25;
    public static final int TIME_UNTIL_FALL_LOWER_BOUND = 5;

    private final Vector2 startingPosition;
    private final GameObjectCollection gameObjects;
    private final int layer;
    private final Random random;


    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     *                      the GameObject will not be rendered.
     * @param gameObjects collection of game objects.
     * @param layer The layer of the leaf.
     */
    public Leaf(Vector2 topLeftCorner, GameObjectCollection gameObjects, int layer, Random random) {
        super(
                topLeftCorner,
                new RectangleRenderable(ColorSupplier.approximateColor(new Color(50,200, 30))));
        this.startingPosition = topLeftCorner;
        this.gameObjects = gameObjects;
        this.layer = layer;
        this.random = random;
        this.setTag("leaf");


        var moveInWindTask = getMoveInWindTask();
        var leafFallTask = getLeafFallTask();

    }

    private ScheduledTask getLeafFallTask() {
        return new ScheduledTask(
                this,
                random.nextFloat(TIME_UNTIL_FALL_LOWER_BOUND, TIME_UNTIL_FALL_UPPER_BOUND),
                false,
                this::leafFallRunnable
        );
    }

    private void leafFallRunnable() {
        this.transform().setVelocityY(FALL_VELOCITY);
        var sideToSideMovementTransition = moveLeafFromSideToSideOnFallTransition();
        this.renderer().fadeOut(
                FADEOUT_TIME,
                this::regenerateLeafTask);

        setCollisionEnterCallBack((other, col)->{
            if(other.getTag().equals("ground")){
                removeComponent(sideToSideMovementTransition);
                    transform().setVelocityY(0);
                    transform().setVelocityX(0);

            }
        });
    }

    private Transition<Float> moveLeafFromSideToSideOnFallTransition() {
        return new Transition<Float>(
                this,
                transform()::setVelocityX,
                50f,
                -50f,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                0.7f,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null
        );
    }

    private void regenerateLeafTask() {
        new ScheduledTask(
                this,
                random.nextFloat(0, 5),
                false,
                this::removeExistingLeafAndCreateNew
        );
    }

    private void removeExistingLeafAndCreateNew() {
        this.setTopLeftCorner(startingPosition);
        this.renderer().fadeIn(1, ()->{
            var moveInWindTask = getMoveInWindTask();
            var leafFallTask = getLeafFallTask();
        });
    }

    private ScheduledTask getMoveInWindTask() {
        return new ScheduledTask(
                this,
                random.nextFloat(),
                false,
                () -> {
                    this.rotationTransition(this);
                    sizeTransition(this);
                }

        );
    }

    private static void sizeTransition(Leaf leaf) {
        new Transition<Vector2>(
                leaf,
                leaf::setDimensions,
                Vector2.of(Block.SIZE, Block.SIZE),
                leaf.getDimensions().multX(0.9f),
                Transition.CUBIC_INTERPOLATOR_VECTOR,
                0.9f,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
    }

    private  void rotationTransition(Leaf leaf) {
        new Transition<Float>(
                leaf,
                leaf.renderer()::setRenderableAngle,
                random.nextFloat(-5f, 0f),
                random.nextFloat(-5f, 25f),
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                random.nextFloat(0.5f, 1.5f),
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
    }
}
