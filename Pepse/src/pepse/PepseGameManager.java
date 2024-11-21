package pepse;

import danogl.GameManager;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Camera;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Trees;


import java.awt.*;
import java.util.Random;

public class PepseGameManager extends GameManager {


    public static void main(String[] args) {
        if(args.length < 1){
            throw new Error("missing arguments");
        }
        String seed = args[0];
        new PepseGameManager(seed).run();
    }


    private Random random;
    private final String seed;

    public PepseGameManager(String seed) {
        random = new Random(seed.hashCode());
        this.seed = seed;
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        Sky.create(gameObjects(),
                windowController.getWindowDimensions(),
                PepseLayer.Sky);

        var terrain = new Terrain(
                gameObjects(),
                windowController.getWindowDimensions(),
                PepseLayer.Ground,
                seed);
        terrain.createInRange(0, windowController.getWindowDimensions().x());

        final float cycleLength = 10;
        var night = Night.create(
                gameObjects(),
                windowController.getWindowDimensions(),
                PepseLayer.Night,
                cycleLength);
        var sun = Sun.create(
                gameObjects(),
                windowController.getWindowDimensions(),
                PepseLayer.Sun,
                cycleLength);
        var halo = SunHalo.create(
                gameObjects(),
                sun,
                PepseLayer.SunHalo);
        var trees = new Trees(gameObjects(),
                windowController.getWindowDimensions(),
                PepseLayer.Trees,
                terrain::groundHeightAt,
                seed);
        trees.createInRange(0, windowController.getWindowDimensions().x());

        gameObjects().layers().shouldLayersCollide(PepseLayer.Leaves, PepseLayer.Ground, true);

        var avatar = createAvatar(imageReader, inputListener, windowController, terrain);
        gameObjects().addGameObject(avatar);
//        setCamera(new Camera(avatar, Vector2.ZERO,
//                windowController.getWindowDimensions(), windowController.getWindowDimensions()));
    }

    private static Avatar createAvatar(ImageReader imageReader, UserInputListener inputListener, WindowController windowController, Terrain terrain) {
        var avatarStartingPosX = windowController.getWindowDimensions().x()/2;
        var avatarStartingPosY = terrain.groundHeightAt(avatarStartingPosX) - Block.SIZE;
        var avatarStartingPosition = Vector2.of(avatarStartingPosX, avatarStartingPosY);
        var avatarRenderable = imageReader.readImage("assets/Male-Mage.png", true);
        var animationRenderable = new AnimationRenderable(new String[]{
                "assets/Male-MageMove1.png",
                "assets/Male-MageMove2.png",
        },
                imageReader,
                true,
                0.2);
        Renderable jumpRenderable = imageReader.readImage("assets/Male-MageJump.png", true);

        var avatar = new Avatar(
                avatarStartingPosition,
                avatarRenderable ,
                animationRenderable,
                jumpRenderable,
                inputListener,
                windowController.getWindowDimensions());
        return avatar;
    }
}
