package ascii_art.img_to_char;

import image.Image;

import java.awt.*;
import java.util.*;
import java.util.List;

public class BrightnessImgCharMatcher implements CharactersChooser {
    private final HashMap<Image, Double> cache = new HashMap<>();
    private final Image img;
    private final String font;
    private final HashMap<Double, Character> charForBrightnessCache = new HashMap<>();

    public BrightnessImgCharMatcher(image.Image img, String font){

        this.img = img;
        this.font = font;
    }
    @Override
    public char[][] chooseChars(int numCharsInRow, Character[] charSet){
        var brightnessForChars = brightnessForChars(charSet, font);
        brightnessForChars = linearCharacterBrightnessStretch(brightnessForChars);
        return convertImageToAscii(img, numCharsInRow,brightnessForChars);
    }
    public static List<Map.Entry<Double, Character>> brightnessForChars(Character[] chars, String font){
        java.util.List<Map.Entry<Double, Character>> br = new ArrayList<>();

        for (Character aChar : chars) {
            br.add(new AbstractMap.SimpleEntry<>(brightnessForChar(aChar, font), aChar));
        }
        return br;
    }

    private static double brightnessForChar(char character, String font) {
        var rendered = CharRenderer.getImg(character, 16, font);
        double sum = 0;
        double amount  = 0;
        for (int row = 0; row < rendered.length; row++) {
            for (int col = 0; col < rendered[row].length; col++) {
                sum += rendered[row][col] ? 1 : 0;
                amount++;
            }
        }
        return sum /amount;
    }

    private List<Map.Entry<Double,Character>> linearCharacterBrightnessStretch(List<Map.Entry<Double,Character>> brightForChars){
        var newBrights = new ArrayList<Map.Entry<Double,Character>>();
        //get min and max values
        var min = brightForChars.getFirst().getKey();
        var max = brightForChars.getFirst().getKey();
        for (Map.Entry<Double, Character> brightForChar : brightForChars) {
            if(brightForChar.getKey() < min){
                min = brightForChar.getKey();
            }
            if(brightForChar.getKey() > max){
                max = brightForChar.getKey();
            }
        }
        for (Map.Entry<Double, Character> brightForChar : brightForChars) {
            newBrights.add(new AbstractMap.SimpleEntry<>(linearStretch(min, max, brightForChar.getKey()),
                    brightForChar.getValue()));
        }

        return newBrights;
    }

    private static double linearStretch(double min, double max, double input) {
        return (input - min) / (max - min);
    }

    public double brightnessForImg(image.Image img){

        if(cache.containsKey(img)){
            return cache.get(img);
        }

        var count = 0;
        double sumBright = 0;
        for(var pixel : img.pixels()){
            count++;
            var greyed = getGreyForColorNormalized(pixel);
            sumBright += greyed;
        }
        
        var brightness = sumBright / count; 
        cache.put(img,brightness) ;

        return brightness;
    }

    private static double getGreyForColorNormalized(Color pixel) {
        return getGreyForColor(pixel) / 255;
    }

    private static double getGreyForColor(Color color) {
        return color.getRed() * 0.2126 + color.getGreen() * 0.7152 + color.getBlue() * 0.0722;
    }

    public char[][] convertImageToAscii(image.Image img, int desiredNumOfChars, List<Map.Entry<Double,
            Character>> brightForChars){
        var asciiImage = new char[desiredNumOfChars][desiredNumOfChars];
        var pixelsForEachSubImage = img.getWidth()/desiredNumOfChars;
        var subSquareImages = img.squareSubImagesOfSize(pixelsForEachSubImage);
        var index = 0;
        charForBrightnessCache.clear();
        for (Image subsquareImage : subSquareImages) {
            var brightness = brightnessForImg(subsquareImage);
            var charForImg = closestCharForBrightness(brightness, brightForChars);
            asciiImage[index / desiredNumOfChars][index % desiredNumOfChars] = charForImg;
            index++;
        }

        return asciiImage;
    }

    private char closestCharForBrightness(double brightness, List<Map.Entry<Double, Character>> brightForChars) {
        double gap = 2;
        if (charForBrightnessCache.containsKey(brightness)){
            System.out.print("ch-");
            return charForBrightnessCache.get(brightness);
        }

        var result = brightForChars.getFirst().getValue();

        for (Map.Entry<Double, Character> brightForChar : brightForChars) {
            var charBright = brightForChar.getKey();
            var currentGap = Math.abs(brightness - charBright);
            if(currentGap < gap){
                gap = currentGap;
                result = brightForChar.getValue();
            }
        }

        charForBrightnessCache.put(brightness, result);
        return result;
    }
}
