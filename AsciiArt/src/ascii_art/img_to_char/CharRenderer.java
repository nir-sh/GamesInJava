package ascii_art.img_to_char;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The class renders (draws) characters to a binary "image" (2D array of booleans).
 * @author Dan Nirel
 */
public class CharRenderer {
    private static final double X_OFFSET_FACTOR = 0.2;
    private static final double Y_OFFSET_FACTOR = 0.75;

    /**
     * Renders a given character, according to how it looks in the specified font,
     * to a square black&white image (2D array of booleans),
     * whose dimension in pixels is as specified.
     * @param c the character to render
     * @param pixels number of pixels in both dimensions of the resulting image
     * @param fontName the name of font by which to draw characters. should be known by the
     *                 operating system.
     */
    public static boolean[][] getImg(char c, int pixels, String fontName) {
        return render(c, pixels, fontName);
    }

    private static boolean[][] render(char c, int pixels, String fontName) {
        String charStr = Character.toString(c);
        Font font = new Font(fontName, Font.PLAIN, pixels);
        BufferedImage img = new BufferedImage(pixels, pixels, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = img.getGraphics();
        graphics.setFont(font);
        int xOffset = (int)Math.round(pixels*X_OFFSET_FACTOR);
        int yOffset = (int)Math.round(pixels*Y_OFFSET_FACTOR);
        graphics.drawString(charStr, xOffset, yOffset);
        boolean[][] matrix = new boolean[pixels][pixels];
        for(int y = 0 ; y < pixels ; y++) {
            for(int x = 0 ; x < pixels ; x++) {
                matrix[y][x] = img.getRGB(x, y) == 0; //is the color black
            }
        }
        return matrix;
    }

    public static void printBoolArr(boolean[][] cs) {
        for(var row = 0; row < cs.length; ++row){
            for(var col = 0; col < cs[row].length; ++col){
                if(cs[row][col]){
                    System.out.print(' ');
                }
                else{
                    System.out.print('#');
                }
            }
            System.out.print('\n');
        }
    }
}
