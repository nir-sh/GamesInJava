package gameOBjects;

import danogl.GameObject;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;
import java.awt.image.renderable.RenderedImageFactory;

public class Heart extends GameObject {
    private final Vector2 topLeftCorner;
    private final Vector2 dimensions;
    private final Renderable renderable;
    private int numLives;


    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, int numLives) {
        super(topLeftCorner, dimensions, renderable);

        this.topLeftCorner = topLeftCorner;
        this.dimensions = dimensions;
        this.renderable = renderable;
        this.numLives = numLives;
    }


    public void setNumLives(int numLives) {
        this.numLives = numLives;
    }


    @Override
    public void render(Graphics2D g) {

        // Render hearts based on the number of lives
        for (int i = 0; i < numLives; i++) {
            // Render heart image at appropriate position
            // You can use different images or draw shapes for hearts
            renderable.render(g,
                    new Vector2(topLeftCorner.x() + i * dimensions.x(),
                            topLeftCorner.y()), dimensions);

        }
    }

    public int getMumLives() {
        return numLives;
    }
}

