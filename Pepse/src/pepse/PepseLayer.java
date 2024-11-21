package pepse;

import danogl.collisions.Layer;

public final class PepseLayer {
    public static final int
    Sky= Layer.BACKGROUND,
    Sun= Layer.BACKGROUND + 1,
    Trees= Layer.BACKGROUND + 3,
    Leaves= Layer.BACKGROUND + 5,
    SunHalo= Layer.BACKGROUND + 10,
    Ground = Layer.STATIC_OBJECTS,
    Night= Layer.FOREGROUND;
    private PepseLayer() { }
}
