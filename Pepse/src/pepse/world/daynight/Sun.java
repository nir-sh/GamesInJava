package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class Sun {

    private static final float SUN_DIAMETER = 100;
    public static GameObject create(GameObjectCollection gameObjects, Vector2 windowDimensions, int layer,
                                    float cycleLength){
        var renderable = new OvalRenderable(Color.YELLOW);
        var midday = new Vector2(windowDimensions.x() * 0.5f - SUN_DIAMETER/2, 0);
        var sun = new GameObject(midday, Vector2.ONES.mult(SUN_DIAMETER), renderable);
        sun.setTag("sun");
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        var mid = new Vector2(windowDimensions.x() * 0.5f, windowDimensions.y() * 0.5f);
        var transition = new Transition<Float>(
                sun,
                (ang)-> sun.setCenter(mid.add(Vector2.UP.mult(windowDimensions.y()/2).rotated(-ang))),//.add(Vector2.UP
                // .multY
                // (windowDimensions.y() * 0
                // .1f))
                // .rotated(ang)),
                0f,
                360f,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength,
                Transition.TransitionType.TRANSITION_LOOP,
                null
        );

        gameObjects.addGameObject(sun, layer);
        return sun;
    }
}
