package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.PepseLayer;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

public class Trees {
    private final GameObjectCollection gameObjects;
    private final int layer;
    private final Function<Float, Float> groundAtHeight;
    private final double highestYForTree;
    private final Random random;
    private final String seed;

    public Trees(GameObjectCollection gameObjects, Vector2 windowDimensions, int layer,
                 Function<Float,Float> groundAtHeight, String seed){
        this.gameObjects = gameObjects;
        this.layer = layer;

        this.groundAtHeight = groundAtHeight;
        highestYForTree = windowDimensions.y() * 0.3;
        this.random = new Random(seed.hashCode());
        this.seed = seed;


    }
    public void createInRange(float minX, float maxX){
        random.setSeed(Objects.hash(minX, seed.hashCode()));
        for (int x = (int) minX; x <= maxX; x += (int) Block.SIZE){
            var isRelevantPlaceForTree = random.nextInt(100) <= 10; //generate tree only for 10% of blocks
            if(!isRelevantPlaceForTree){
                continue;
            }

            var yOfGround = groundAtHeight.apply((float) x);
            float MIN_HEIGHT_FOR_TREE = 100;
            var treeMinHeight = yOfGround - MIN_HEIGHT_FOR_TREE;
            var treeHighestPoint = random.nextInt((int) (treeMinHeight - highestYForTree)) + highestYForTree;
            int treeHighestPointInBlocks = (int) (treeHighestPoint / Block.SIZE) * (int) Block.SIZE;
            //generate blocks for tree trunk from high to ground
            for (int y = treeHighestPointInBlocks; y <= yOfGround; y += (int) Block.SIZE) {

                var treeBlock = new Block(
                        new Vector2(x, y),
                        new RectangleRenderable(ColorSupplier.approximateColor(new Color(100, 50, 20)))
                );
                treeBlock.setTag("tree");
                treeBlock.physics().preventIntersectionsFromDirection(Vector2.ZERO);
                treeBlock.physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
                gameObjects.addGameObject(treeBlock, layer);


            }
            // Create leaves for tree
            createTreeTop(treeHighestPointInBlocks, yOfGround, x);


        }
    }

    private void createTreeTop(int treeHighestPointInBlocks, Float yOfGround, int x) {
        int treeHeight = (int) Math.abs(treeHighestPointInBlocks - yOfGround);
        int treetopSize = (int) (treeHeight * 0.6); // Size of leaves is proportional to the height of the tree
        int treetopStartX = x - (treetopSize / 2); // Start X coordinate of leaves rectangle
        int treetopStartY = treeHighestPointInBlocks - (treetopSize / 2); // Start Y coordinate of leaves rectangle
        int treetopEndX = treetopStartX + treetopSize; // End X coordinate of leaves rectangle
        int treetopEndY = treeHighestPointInBlocks + (treetopSize / 2); // End Y coordinate of leaves rectangle

        for (int leafX = treetopStartX; leafX <= treetopEndX; leafX += (int) Block.SIZE) {
            for (int leafY = treetopStartY; leafY <= treetopEndY; leafY += (int) Block.SIZE) {

                createNewLeaf(leafX, leafY);
            }
        }
    }

    private void createNewLeaf(int leafX, int leafY) {
        var leavesLayer = PepseLayer.Leaves;
        var leaf = new Leaf(
                new Vector2(leafX, leafY),
                gameObjects,
                leavesLayer,
                random);
        gameObjects.addGameObject(leaf, leavesLayer);
    }



}
