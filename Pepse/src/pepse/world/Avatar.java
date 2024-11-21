package pepse.world;

import danogl.GameObject;
import danogl.components.movement_schemes.PlatformerMovementScheme;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Avatar extends GameObject {
    private static final float GRAVITY = 600;
    private static final float AVATAR_BLOCK_SIZE_FACTOR = 1.8f;
    private static final float HORIZONTAL_VEL = 300;
    private static final float JUMP_VEL = -600;
    private final Renderable defaultRenderable;
    private final AnimationRenderable animationRenderable;
    private final UserInputListener inputListener;
    private final PlatformerMovementScheme movementScheme;
    private Vector2 windowDimensions;
    private Renderable jumpRenderable;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Avatar(Vector2 topLeftCorner,
                  Renderable renderable,
                  AnimationRenderable animationRenderable,
                  Renderable jumpRenderable,
                  UserInputListener inputListener,
                  Vector2 windowDimensions) {
        super(topLeftCorner, Vector2.ONES.mult(Block.SIZE * AVATAR_BLOCK_SIZE_FACTOR), renderable);
        this.defaultRenderable = renderable;
        this.animationRenderable = animationRenderable;
        this.inputListener = inputListener;
        movementScheme = new PlatformerMovementScheme(
                this,
                inputListener.keyboardMovementDirector(),
                HORIZONTAL_VEL,
                JUMP_VEL);
        this.windowDimensions = windowDimensions;
        this.jumpRenderable = jumpRenderable;
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(300);
        transform().setAccelerationY(GRAVITY);

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        movementScheme.update(deltaTime);


        var currentPosition = this.getTopLeftCorner();
        var velocityX = this.getVelocity().x();

        // Wrap around the screen horizontally
        if (currentPosition.x() >= windowDimensions.x() && velocityX > 0) {
            this.setTopLeftCorner(Vector2.of(0, currentPosition.y()));
        } else if (currentPosition.x() <= 0 && velocityX < 0) {
            this.setTopLeftCorner(Vector2.of(windowDimensions.x(), currentPosition.y()));
        }

        //set renderable based on movement
        if(velocityX == 0){
            renderer().setRenderable(defaultRenderable);
        }
        else {
            // Set horizontal flip based on velocity direction
            this.renderer().setIsFlippedHorizontally(velocityX < 0);
            renderer().setRenderable(animationRenderable);
        }

        if(getVelocity().y() < 0){
            this.renderer().setRenderable(jumpRenderable);
        }
    }
}
