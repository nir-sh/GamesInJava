package ascii_art.img_to_char;

import image.Image;
import org.junit.Assert;

import static org.junit.jupiter.api.Assertions.*;

class BrightnessImgCharMatcherTest {

    @org.junit.jupiter.api.Test
    void chooseChars() {
        var br = new BrightnessImgCharMatcher(Image.fromFile("board.jpeg"), "Ariel");
        var charsSet = new Character[]{'m','o'};
        var expected = new char[][]{
                {'m','o'},
                {'o','m'}
        };
        assertArrayEquals(expected, br.chooseChars(2,charsSet));
    }

    @org.junit.jupiter.api.Test
    void brightnessForChars() {
    }

    @org.junit.jupiter.api.Test
    void brightnessForImg() {
    }

    @org.junit.jupiter.api.Test
    void convertImageToAscii() {
    }
}