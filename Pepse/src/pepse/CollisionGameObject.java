package pepse;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.function.BiConsumer;

public class CollisionGameObject extends GameObject {
    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public CollisionGameObject(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
    }

    public void setCollisionEnterCallBack(BiConsumer<GameObject, Collision> collisionCallBack) {
        this.collisionCallBack = collisionCallBack;
    }

    private BiConsumer<GameObject , Collision> collisionCallBack;

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        if (collisionCallBack != null){
            collisionCallBack.accept(other,collision);
        }
    }
}
