package gameOBjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

public class UserPaddle extends GameObject {
    private static final float MOVEMENT_SPEED = 450   ;
    private static final float MIN_DISTANCE_FROM_SCREEN_EDGE = 14;
    private final UserInputListener inputListener;
    private Vector2 windowDimensions;

    public UserPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable paddleImage,
                      UserInputListener inputListener, Vector2 windowDimensions) {
        super(topLeftCorner, dimensions, paddleImage);
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // Ensure the paddle does not go beyond the left edge of the screen
        float minX = Math.max(MIN_DISTANCE_FROM_SCREEN_EDGE, getTopLeftCorner().x());
        float maxX = windowDimensions.x() - MIN_DISTANCE_FROM_SCREEN_EDGE - getDimensions().x();

        // Ensure the paddle does not go beyond the right edge of the screen
        float newX = Math.min(maxX, minX);

        setTopLeftCorner(new Vector2(newX, getTopLeftCorner().y()));

        // Calculate movement direction based on input
        float movementX = 0;

        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            movementX -= 1;
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            movementX += 1;
        }

        // Set velocity based on movement direction
        setVelocity(new Vector2(movementX * MOVEMENT_SPEED, 0));
    }
}
