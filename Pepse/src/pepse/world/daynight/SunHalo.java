package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

public class SunHalo{

    private final static float DIAMETER = 300;

    public static GameObject create(GameObjectCollection gameObjects, GameObject sun, int layer){
        var halo = new GameObject(sun.getCenter().subtract(Vector2.of(-DIAMETER, -DIAMETER)), Vector2.of(DIAMETER, DIAMETER),
                new OvalRenderable(new Color(255, 255, 0, 20)));
        halo.setTag("halo");
        halo.addComponent(time-> halo.setCenter(sun.getCenter()));
        halo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(halo, layer);
        return halo;
    }
}
