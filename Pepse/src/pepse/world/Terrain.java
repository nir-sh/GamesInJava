package pepse.world;

import ca.grimoire.jnoise.modules.basic.Gradient;
import ca.grimoire.jnoise.modules.generation.Perlin;
import danogl.collisions.GameObjectCollection;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;
import java.util.Random;

public class Terrain {
    private final GameObjectCollection gameObjects;
    private final Vector2 windowDimensions;
    private final int layer;
    private final Random random;
    private final Perlin perlin;

    private float groundHeightAtX0;
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    public Terrain(GameObjectCollection gameObjects, Vector2 windowDimensions, int layer, String seed){

        this.gameObjects = gameObjects;
        this.windowDimensions = windowDimensions;
        this.layer = layer;
        groundHeightAtX0 = windowDimensions.y() * 0.6666f;
        // Seed the random generator with x to get consistent results
        this.random = new Random(seed.hashCode());

        // Example instantiation of Perlin noise generator
        perlin = new Perlin(
                seed.hashCode(),
                8,
                1.5,
                0.5,
                Gradient.Quality.HIGH
                );
    }

    public float groundHeightAt(float x){
        if(Float.compare(x, 0) == 0)
            return groundHeightAtX0;
        double noise = perlin.getValue(x+0.1, 0, 0);
        var rawHeight = ((float) noise * 100) + groundHeightAtX0;
        return (int) (rawHeight / Block.SIZE) * (int) Block.SIZE;
    }

    public void createInRange(float minX, float maxX){

        for (int x = (int) minX; x <= maxX; x += (int) Block.SIZE){
            int startY = (int) groundHeightAt(x);
            for (int y = startY; y <= windowDimensions.y() + 200; y += (int) Block.SIZE) {
                var groundBlock = new Block(
                        new Vector2(x, y),
                        new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR))
                );
                groundBlock.setTag("ground");
                groundBlock.physics().preventIntersectionsFromDirection(Vector2.ZERO);
                groundBlock.physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
                gameObjects.addGameObject(groundBlock, layer);
            }
        }

    }
}
